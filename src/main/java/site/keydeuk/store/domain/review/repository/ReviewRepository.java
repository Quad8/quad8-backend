package site.keydeuk.store.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}