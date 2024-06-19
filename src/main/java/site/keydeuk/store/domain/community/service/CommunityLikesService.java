package site.keydeuk.store.domain.community.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.community.repository.CommunityLikesRepository;

@RequiredArgsConstructor
@Service
public class CommunityLikesService {

    private final CommunityLikesRepository likesRepository;
    @Transactional
    public void deleteAllLikesByCommunityId(Long communityId){
        likesRepository.deleteByCommunity_Id(communityId);
    }

}
