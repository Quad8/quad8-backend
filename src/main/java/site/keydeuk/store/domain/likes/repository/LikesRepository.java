package site.keydeuk.store.domain.likes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.keydeuk.store.entity.Likes;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Page<Likes> findByUserId(Long userId, Pageable pageable);
    Optional<Likes> findByUserIdAndProductId(Long userId, Integer productId);
    Optional<List<Likes>> findByUserIdAndProductIdIn(Long userId, List<Long> productIds);
    boolean existsByUserIdAndProductId(Long userId, Integer productId);
}
