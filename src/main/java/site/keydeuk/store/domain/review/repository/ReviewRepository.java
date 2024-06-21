package site.keydeuk.store.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsReviewByUserIdAndProductId (Long userId, Integer productId);
    List<Review> findByUserId(Long userId);
    List<Review> findByProductId(Integer productId);

    Long countByProductId(Integer productId);

    @Query("SELECT AVG(r.score) FROM Review r WHERE r.product.id = :productId")
    Double findAverageScoreByProductId(Integer productId);
    @Query("SELECT r.score, COUNT(r) FROM Review r WHERE r.product.id = :productId GROUP BY r.score")
    List<Object[]> findScoreCountsByProductId(Integer productId);
    @Query("SELECT r.option1, COUNT(r) FROM Review r WHERE r.product.id = :productId GROUP BY r.option1")
    List<Object[]> findOption1CountsByProductId(Integer productId);

    @Query("SELECT r.option2, COUNT(r) FROM Review r WHERE r.product.id = :productId GROUP BY r.option2")
    List<Object[]> findOption2CountsByProductId(Integer productId);

    @Query("SELECT r.option3, COUNT(r) FROM Review r WHERE r.product.id = :productId GROUP BY r.option3")
    List<Object[]> findOption3CountsByProductId(Integer productId);

}