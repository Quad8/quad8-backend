package site.keydeuk.store.domain.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.keydeuk.store.entity.CommunityComment;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment,Long> {
}
