package site.keydeuk.store.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import site.keydeuk.store.entity.ReviewImg;

import java.util.List;

@Repository
public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM ReviewImg ri WHERE ri.reviewId = :reviewId AND ri.id NOT IN (:existingImgIds)")
    void deleteByReviewIdAndIdNotIn(@Param("reviewId") Long reviewId, @Param("existingImgIds") List<Long> existingImgIds);
}
