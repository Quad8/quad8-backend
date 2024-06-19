package site.keydeuk.store.domain.reviewLikes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.ReviewLikes;

import java.util.Optional;

@Repository
public interface ReviewLikesRepository extends JpaRepository<ReviewLikes, Long> {
    boolean existsByUserIdAndReviewId(Long userId, Long reviewId);

    Optional<ReviewLikes> findByUserIdAndReviewId(Long userId, Long reviewId);
}
