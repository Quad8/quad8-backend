package site.keydeuk.store.domain.community.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.keydeuk.store.domain.community.repository.CommunityImgRepository;
import site.keydeuk.store.domain.image.service.ImageService;
import site.keydeuk.store.entity.CommunityImg;

@RequiredArgsConstructor
@Service
public class CommunityImgService {

    private final CommunityImgRepository imgRepository;
    private final ImageService imageService;
    @Transactional
    public void deleteAllImgByCommunityId(Long communityId){
        List<CommunityImg> list = imgRepository.findAllByCommunityId(communityId);
        for (CommunityImg img : list){
            imageService.deleteImage(img.getImgUrl());
        }
        imgRepository.deleteByCommunity_Id(communityId);


    }
}
