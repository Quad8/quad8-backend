package site.keydeuk.store.domain.reviewLikes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.ReviewLikes;

@Repository
public interface ReviewLikesRepository extends JpaRepository<ReviewLikes, Long> {
    boolean existsByUserIdAndReviewId(Long userId, Long reviewId);
}
