package site.keydeuk.store.domain.likes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.keydeuk.store.entity.Likes;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByUserId(Long userId);
    Optional<Likes> findByUserIdAndProductId(Long userId, Integer productId);
    boolean existsByUserIdAndProductId(Long userId, Integer productId);
}
