package site.keydeuk.store.domain.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.response.ErrorCode;
import site.keydeuk.store.domain.image.service.ImageService;
import site.keydeuk.store.domain.order.repository.OrderItemsRepository;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.review.dto.ReviewDto;
import site.keydeuk.store.domain.review.dto.request.CreateReviewRequest;
import site.keydeuk.store.domain.review.dto.response.ReviewResponse;
import site.keydeuk.store.domain.review.repository.ReviewImgRepository;
import site.keydeuk.store.domain.review.repository.ReviewRepository;
import site.keydeuk.store.domain.reviewLikes.repository.ReviewLikesRepository;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.Product;
import site.keydeuk.store.entity.Review;
import site.keydeuk.store.entity.ReviewImg;
import site.keydeuk.store.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
    public Long createReview(Long userId, Integer productId, CreateReviewRequest createReviewRequest, List<MultipartFile> reviewImgs) {
        getOrderItem(userId, productId);
        Product product = getProduct(productId);
        User user = getUser(userId);

        if (isReviewExistsByUserAndProduct(userId, productId)) {
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

        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public List<Review> getUserReviews(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public ReviewResponse getProductReviews(Integer productId, Long userId) {
        getProduct(productId);
        List<Review> reviews = reviewRepository.findByProductId(productId);
        List<ReviewDto> reviewDtoList = reviews.stream()
                .map(review -> {
                    Long likeCount = reviewLikesRepository.countByReviewId(review.getId());
                    Boolean likedByUser = userId != null && reviewLikesRepository.existsByReviewIdAndUserId(review.getId(), userId);
                    return ReviewDto.of(review, likeCount, likedByUser);
                })
                .toList();

        Double averageScore = reviewRepository.findAverageScoreByProductId(productId);
        Long reviewCounts = reviewRepository.countByProductId(productId);
        Map<String, Map<Integer, Double>> reviewStatistics = getReviewStatistics(productId);
        return ReviewResponse.of(reviewDtoList, averageScore, reviewCounts, reviewStatistics);
    }
    public Map<String, Map<Integer, Double>> getReviewStatistics(Integer productId) {
        Map<String, Map<Integer, Double>> statistics = new HashMap<>();
        statistics.put("scoreRatios", getRatios(reviewRepository.findScoreCountsByProductId(productId),5));
        statistics.put("option1Ratios", getRatios(reviewRepository.findOption1CountsByProductId(productId),3));
        statistics.put("option2Ratios", getRatios(reviewRepository.findOption2CountsByProductId(productId),3));
        statistics.put("option3Ratios", getRatios(reviewRepository.findOption3CountsByProductId(productId),3));
        return statistics;
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

    private void getOrderItem(Long userId, Integer productId) {
        orderItemsRepository.findByOrder_UserIdAndProductId(userId, productId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    private boolean isReviewExistsByUserAndProduct(Long userId, Integer productId) {
        return reviewRepository.existsReviewByUserIdAndProductId(userId, productId);
    }
}
