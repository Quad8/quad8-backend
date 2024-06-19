package site.keydeuk.store.domain.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.CommunityLikes;
@Repository
public interface CommunityLikesRepository extends JpaRepository<CommunityLikes,Long> {
}
