package site.keydeuk.store.domain.communitylikes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.CommunityLikes;
@Repository
public interface CommunityLikesRepository extends JpaRepository<CommunityLikes,Long> {

    CommunityLikes findByUserIdAndCommunityId(Long userId, Long communityId);
    boolean existsByUserIdAndCommunityId(Long userId, Long communityId);
    void deleteByCommunity_Id(Long id);
}
