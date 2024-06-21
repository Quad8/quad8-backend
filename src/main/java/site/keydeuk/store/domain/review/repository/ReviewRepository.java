package site.keydeuk.store.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsReviewByUserIdAndProductIdAndOrderId (Long userId, Integer productId, Long orderId);
    List<Review> findByUserId(Long userId);
    Page<Review> findByProductId(Integer productId, Pageable pageable);
    @Query("SELECT r FROM Review r LEFT JOIN ReviewLikes rl ON r.id=rl.review.id WHERE r.product.id = :productId GROUP BY r.id ORDER BY COUNT(rl.id) DESC")
    Page<Review> findByProductIdOrderByLikes(@Param("productId") Integer productId, Pageable pageable);

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