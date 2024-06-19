package site.keydeuk.store.domain.community.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.community.repository.CommunityCommentRepository;

@RequiredArgsConstructor
@Service
public class CommunityCommentService {
    private final CommunityCommentRepository commentRepository;

    @Transactional
    public void deleteAllCommentByCommunityId(Long communityId){
        commentRepository.deleteByCommunity_Id(communityId);
    }
}
