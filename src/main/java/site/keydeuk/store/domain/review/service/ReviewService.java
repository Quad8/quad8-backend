package site.keydeuk.store.domain.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.redis.service.RedisService;
import site.keydeuk.store.common.response.ErrorCode;
import site.keydeuk.store.domain.image.service.ImageService;
import site.keydeuk.store.domain.order.repository.OrderItemsRepository;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.productswitchoption.repository.ProductSwitchOptionRepository;
import site.keydeuk.store.domain.review.dto.ReviewDto;
import site.keydeuk.store.domain.review.dto.ReviewImgDto;
import site.keydeuk.store.domain.review.dto.request.CreateReviewRequest;
import site.keydeuk.store.domain.review.dto.request.UpdateReviewRequest;
import site.keydeuk.store.domain.review.dto.response.ReviewResponse;
import site.keydeuk.store.domain.review.dto.response.UserReviewResponse;
import site.keydeuk.store.domain.review.repository.ReviewImgRepository;
import site.keydeuk.store.domain.review.repository.ReviewRepository;
import site.keydeuk.store.domain.reviewLikes.repository.ReviewLikesRepository;
import site.keydeuk.store.domain.user.dto.response.ReviewUserResponse;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.*;

import java.time.LocalDateTime;
import java.util.*;

import static site.keydeuk.store.common.response.ErrorCode.COMMON_INVALID_PARAMETER;
import static site.keydeuk.store.common.response.ErrorCode.OPTION_NOT_FOUND;
import static site.keydeuk.store.domain.product.service.ProductService.PRODUCT_KEY_PREFIX;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ImageService imageService;
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final ReviewLikesRepository reviewLikesRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductSwitchOptionRepository productSwitchOptionRepository;

    private final RedisService redisService;

    @Transactional
    public Long createReview(Long userId, Integer productId, CreateReviewRequest createReviewRequest, List<MultipartFile> reviewImgs) {
        validateOrderItem(userId, productId, createReviewRequest.orderId());
        Product product = getProduct(productId);
        User user = getUser(userId);

        if (isReviewExistsByUserAndProduct(userId, productId, createReviewRequest.orderId())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_REVIEW);
        }

        Review review = createReviewRequest.toEntity(createReviewRequest, user, product);

        if (reviewImgs == null || reviewImgs.isEmpty()) {
            Review savedReview = reviewRepository.save(review);
            return savedReview.getId();
        }

        Review savedReview = reviewRepository.save(review);
        List<String> imageUrls = imageService.uploadReviewImages(reviewImgs);
        List<ReviewImg> reviewImgList = imageUrls.stream()
                .map(url -> new ReviewImg(url, savedReview.getId()))
                .toList();
        review.addReviewImgs(reviewImgList);

        // 리뷰 작성 시 product 상세보기 cahce 삭제
        deleteCacheProductDedatil(productId);

        return savedReview.getId();
    }

    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(ErrorCode.REVIEW_NOT_FOUND)
        );

        if (!review.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }

        int productId = review.getProduct().getId();
        reviewRepository.delete(review);

        // 리뷰 삭제 시 product 상세보기 cahce 삭제
        deleteCacheProductDedatil(productId);
    }

    @Transactional(readOnly = true)
    public UserReviewResponse getUserReviews(Long userId, Pageable pageable, LocalDateTime startDate, LocalDateTime endDate) {
        Page<Review> reviews = reviewRepository.findByUserIdAndCreatedAtBetween(userId, startDate, endDate, pageable);
        log.info("{}", reviews.getContent());
        List<ReviewDto> reviewDtos = getReviewDtos(userId, reviews.getContent(), reviews.getPageable());
        long reviewCounts = reviews.getTotalElements();
        int totalPages = reviews.getTotalPages();
        int currentPage = reviews.getNumber();
        boolean isFirst = reviews.isFirst();
        boolean isLast = reviews.isLast();
        return UserReviewResponse.of(reviewDtos, reviewCounts, totalPages,currentPage, isFirst, isLast);
    }

    @Transactional(readOnly = true)
    public ReviewResponse getProductReviews(Integer productId, Long userId, Pageable pageable, String sort) {
        getProduct(productId);
        Page<Review> reviews;
        switch (sort) {
            case "createdAt" -> {
                Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, sort);
                reviews = reviewRepository.findByProductId(productId, newPageable);
            }
            case "likes" -> {
                reviews = reviewRepository.findByProductIdOrderByLikes(productId, pageable);
            }
            case "scoreHigh" -> {
                Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "score");
                reviews = reviewRepository.findByProductId(productId, newPageable);
            }
            case "scoreLow" -> {
                Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, "score");
                reviews = reviewRepository.findByProductId(productId, newPageable);
            }
            default -> {
                log.error("Invalid sort parameter: {}", sort);
                throw new CustomException(COMMON_INVALID_PARAMETER);
            }
        }
        List<ReviewDto> reviewDtoList = getReviewDtos(userId, reviews.getContent(), reviews.getPageable());

        Double averageScore = reviewRepository.findAverageScoreByProductId(productId);
        Map<String, Map<Integer, Double>> reviewStatistics = getReviewStatistics(productId);
        Long reviewCounts = reviews.getTotalElements();
        int totalPages = reviews.getTotalPages();
        int currentPage = reviews.getNumber();
        boolean isFirst = reviews.isFirst();
        boolean isLast = reviews.isLast();
        return ReviewResponse.of(reviewDtoList, averageScore, reviewCounts, reviewStatistics, totalPages,currentPage, isFirst, isLast);
    }

    private List<ReviewDto> getReviewDtos(Long userId, List<Review> reviews, Pageable pageable) {
        return reviews.stream()
                .map(review -> {
                    String switchOptionName = getSwitchOptionName(review);
                    ReviewUserResponse writer = ReviewUserResponse.from(review.getUser());
                    Long likeCount = reviewLikesRepository.countByReviewId(review.getId());
                    Boolean likedByUser = userId != null && reviewLikesRepository.existsByReviewIdAndUserId(review.getId(), userId);
                    return ReviewDto.of(review, switchOptionName, writer, likeCount, likedByUser, pageable);
                })
                .toList();
    }

    private String getSwitchOptionName(Review review) {
        OrderItem orderItem = getOrderItem(review.getUser().getId(), review.getProduct().getId(), review.getOrderId());
        Long switchOptionId = orderItem.getSwitchOptionId();
        if (switchOptionId != null) {
            ProductSwitchOption productSwitchOption = productSwitchOptionRepository.findById(switchOptionId).orElseThrow(
                    () -> new CustomException(OPTION_NOT_FOUND)
            );
            return productSwitchOption.getOptionName();
        }
        return "";
    }

    public Map<String, Map<Integer, Double>> getReviewStatistics(Integer productId) {
        Map<String, Map<Integer, Double>> statistics = new HashMap<>();
        statistics.put("scoreRatios", getRatios(reviewRepository.findScoreCountsByProductId(productId), 5));
        statistics.put("option1Ratios", getRatios(reviewRepository.findOption1CountsByProductId(productId), 3));
        statistics.put("option2Ratios", getRatios(reviewRepository.findOption2CountsByProductId(productId), 3));
        statistics.put("option3Ratios", getRatios(reviewRepository.findOption3CountsByProductId(productId), 3));
        return statistics;
    }

    @Transactional
    public Long updateReview(Long userId, Long reviewId, UpdateReviewRequest updateReviewRequest, List<MultipartFile> reviewImgs) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(ErrorCode.REVIEW_NOT_FOUND)
        );

        if (!review.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }

        review.update(updateReviewRequest.content(), updateReviewRequest.score(), updateReviewRequest.option1(), updateReviewRequest.option2(), updateReviewRequest.option3());

        List<Long> existingImgIds = updateReviewRequest.existingReviewImgs().stream()
                .map(ReviewImgDto::id)
                .toList();

        reviewImgRepository.deleteByReviewIdAndIdNotIn(reviewId, existingImgIds);

        if (reviewImgs != null && !reviewImgs.isEmpty()) {
            List<String> newImageUrls = imageService.uploadReviewImages(reviewImgs);
            List<ReviewImg> newReviewImgs = newImageUrls.stream()
                    .map(url -> new ReviewImg(url, review.getId()))
                    .toList();
            review.addReviewImgs(newReviewImgs);
        }

        Review updatedReview = reviewRepository.save(review);
        return updatedReview.getId();
    }

    public Double getAverageScoreByProductId(Integer id){
        Double scope = (double) 0;
        if (reviewRepository.findAverageScoreByProductId(id) != null) scope = reviewRepository.findAverageScoreByProductId(id);
        return scope;
    }

    public Long countByProductId(Integer id){
        return reviewRepository.countByProductId(id);
    }
    private Map<Integer, Double> getRatios(List<Object[]> counts, int maxValue) {
        Map<Integer, Long> countMap = new HashMap<>();
        long totalCount = 0;
        int size = 0;
        for (Object[] count : counts) {
            Integer key = (Integer) count[0];
            Long value = (Long) count[1];
            countMap.put(key, value);
            totalCount += value;
            size++;
            log.info("{}", size);
        }

        Map<Integer, Double> ratios = new HashMap<>();
        for (int i = 1; i <= maxValue; i++) {
            Long count = countMap.getOrDefault(i, 0L);
            double ratio = (totalCount == 0) ? 0.0 : (double) count / totalCount * 100;
            ratios.put(i, ratio);
        }

        return ratios;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }

    private Product getProduct(Integer productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );
    }

    private void validateOrderItem(Long userId, Integer productId, Long orderId) {
        List<OrderItem> orderItems = orderItemsRepository.findByOrder_UserIdAndProductId(userId, productId);
        log.info("{}", orderItems);
        boolean orderExists = orderItems.stream()
                .anyMatch(orderItem -> orderItem.getOrder().getId().equals(orderId));

        if (!orderExists) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
    }

    private OrderItem getOrderItem(Long userId, Integer productId, Long orderId) {
        validateOrderItem(userId, productId, orderId);
        return orderItemsRepository.findByOrderIdAndProductId(orderId, productId);
    }

    private boolean isReviewExistsByUserAndProduct(Long userId, Integer productId, Long orderId) {
        return reviewRepository.existsReviewByUserIdAndProductIdAndOrderId(userId, productId, orderId);
    }

    /** 상품 상세보기 Cache 삭제 */
    private void deleteCacheProductDedatil(int productId){
        String key = PRODUCT_KEY_PREFIX+productId;
        redisService.delete(key);
    }
}
