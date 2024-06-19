package site.keydeuk.store.domain.reviewLikes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.review.repository.ReviewRepository;
import site.keydeuk.store.domain.reviewLikes.repository.ReviewLikesRepository;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.*;

import static site.keydeuk.store.common.response.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReviewLikesService {
    private final ReviewRepository reviewRepository;
    private final ReviewLikesRepository reviewLikesRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewLikes addLikes(Long userId, Long reviewId) {
        if (reviewLikesRepository.existsByUserIdAndReviewId(userId, reviewId)) {
            throw new CustomException(ALREADY_EXIST_LIKE);
        }
        User user = getUser(userId);
        Review review = getReview(reviewId);
        ReviewLikes reviewLikes = ReviewLikes.builder()
                .user(user)
                .review(review)
                .build();

        return reviewLikesRepository.save(reviewLikes);
    }

    @Transactional
    public void deleteLike(Long userId, Long reviewId) {
        ReviewLikes reviewLike = reviewLikesRepository.findByUserIdAndReviewId(userId, reviewId)
                .orElseThrow(() -> new CustomException(LIKES_NOT_FOUND));
        reviewLikesRepository.delete(reviewLike);
    }

    private Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
