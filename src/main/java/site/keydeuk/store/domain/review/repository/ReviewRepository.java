package site.keydeuk.store.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsReviewByUserIdAndProductId (Long userId, Integer productId);

    List<Review> findByUserId(Long userId);
}