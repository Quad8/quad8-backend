package site.keydeuk.store.domain.likes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.keydeuk.store.entity.Likes;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByUserId(Long userId);
    List<Likes> findByProductId(Long productId);
    boolean existsByUserIdAndProductId(Long userId, Integer productId);
}
