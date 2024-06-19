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

    private Review getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));
        return review;
    }

    private User getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return user;
    }
}
