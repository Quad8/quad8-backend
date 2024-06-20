package site.keydeuk.store.domain.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.CommunityImg;

@Repository
public interface CommunityImgRepository extends JpaRepository<CommunityImg,Long> {
    void deleteByCommunity_Id(Long id);

    void deleteByImgUrl(String url);
}
