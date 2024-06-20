package site.keydeuk.store.domain.communitycomment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.CommunityComment;

import java.util.List;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment,Long> {

    List<CommunityComment> findByCommunity_Id(Long communityId);

    Long countByCommunity_Id(Long communityId);

    void deleteByCommunity_Id(Long id);

}
