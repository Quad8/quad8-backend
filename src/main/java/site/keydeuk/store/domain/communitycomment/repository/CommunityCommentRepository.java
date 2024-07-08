package site.keydeuk.store.domain.communitycomment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.CommunityComment;

import java.util.List;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment,Long> {

    List<CommunityComment> findByCommunity_Id(Long communityId, Pageable pageable);

    Long countByCommunity_Id(Long communityId);

    void deleteByCommunity_Id(Long id);

    @Query(value = "select c from CommunityComment c where c.community.id = :communityId and c.id > :commentId")
    Page<CommunityComment> findByCommunityCommentIdLessThan(@Param("communityId")Long communityId,@Param("commentId")Long commentId, PageRequest pageRequest);

}
