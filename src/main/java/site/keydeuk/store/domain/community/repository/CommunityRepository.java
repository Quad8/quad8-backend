package site.keydeuk.store.domain.community.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.Community;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community,Long> {

    Community findByIdAndUser_Id(Long id, Long userId);
    Community findByCustomOptionId(Integer id);
    Page<Community> findByUserId(Long userId, Pageable pageable);
    @Query("select c from Community c LEFT join CommunityLikes cl on c.id = cl.community.id group by c.id order by count (cl.id) DESC ")
    Page<Community> findAllOrderByLikes(Pageable pageable);
    @Query("select c from Community c order by c.viewCount desc ")
    Page<Community> findAllOrderByViewCount(Pageable pageable);
    @Query("select c from Community c order by c.createdAt desc ")
    Page<Community> findAllOrderByCreatedAt(Pageable pageable);

    @Query("SELECT c FROM Community c LEFT JOIN CommunityLikes cl ON c.id = cl.community.id WHERE c.user.id = :userId GROUP BY c.id ORDER BY COUNT(cl.id) DESC")
    Page<Community> findByUserIdOrderByLikes(Long userId, Pageable pageable);

    @Query("SELECT c FROM Community c WHERE c.user.id = :userId ORDER BY c.viewCount DESC")
    Page<Community> findByUserIdOrderByViewCount(Long userId, Pageable pageable);

    @Query("SELECT c FROM Community c WHERE c.user.id = :userId ORDER BY c.createdAt DESC")
    Page<Community> findByUserIdOrderByCreatedAt(Long userId, Pageable pageable);
}
