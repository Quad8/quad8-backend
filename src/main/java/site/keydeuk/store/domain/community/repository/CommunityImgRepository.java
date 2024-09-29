package site.keydeuk.store.domain.community.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.CommunityImg;

@Repository
public interface CommunityImgRepository extends JpaRepository<CommunityImg,Long> {
    void deleteByCommunity_Id(Long id);

    void deleteByImgUrl(String url);

    CommunityImg findByIdAndCommunity_Id(Long id, Long communityId);

    Long countByCommunity_Id(Long communityId);

    @Query("select c from CommunityImg c where c.community.id = :communityId")
    List<CommunityImg> findAllByCommunityId(Long communityId);

}
