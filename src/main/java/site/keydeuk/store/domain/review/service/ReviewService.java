package site.keydeuk.store.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.response.ErrorCode;
import site.keydeuk.store.domain.image.service.ImageService;
import site.keydeuk.store.domain.order.repository.OrderItemsRepository;
import site.keydeuk.store.domain.order.repository.OrderRepository;
import site.keydeuk.store.domain.product.repository.ProductRepository;
import site.keydeuk.store.domain.review.dto.request.CreateReviewRequest;
import site.keydeuk.store.domain.review.repository.ReviewImgRepository;
import site.keydeuk.store.domain.review.repository.ReviewRepository;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ImageService imageService;
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createReview(Long userId, Integer productId, CreateReviewRequest createReviewRequest, List<MultipartFile> reviewImgs) {
        getOrderItem(userId, productId);

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Review review = createReviewRequest.toEntity(createReviewRequest, user, product);

        if (reviewImgs == null || reviewImgs.isEmpty()) {
            Review savedReview = reviewRepository.save(review);
            return savedReview.getId();
        }

        List<String> imageUrls = imageService.uploadReviewImages(reviewImgs);
        List<ReviewImg> reviewImgList = imageUrls.stream()
                .map(ReviewImg::new)
                .toList();
        review.addReviewImgs(reviewImgList);
        Review savedReview = reviewRepository.save(review);
        return savedReview.getId();
    }

    private void getOrderItem(Long userId, Integer productId) {
        orderItemsRepository.findByOrder_UserIdAndProductId(userId, productId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }
}
