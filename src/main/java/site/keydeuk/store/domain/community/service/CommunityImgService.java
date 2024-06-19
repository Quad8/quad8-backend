package site.keydeuk.store.domain.community.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.community.repository.CommunityImgRepository;

@RequiredArgsConstructor
@Service
public class CommunityImgService {

    private final CommunityImgRepository imgRepository;
    @Transactional
    public void deleteAllImgByCommunityId(Long communityId){
        imgRepository.deleteByCommunity_Id(communityId);
    }
}
