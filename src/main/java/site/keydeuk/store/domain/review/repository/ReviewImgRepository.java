package site.keydeuk.store.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.ReviewImg;

@Repository
public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
}
