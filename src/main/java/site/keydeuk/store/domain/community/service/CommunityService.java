package site.keydeuk.store.domain.community.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.community.dto.create.PostDto;
import site.keydeuk.store.domain.community.repository.CommunityImgRepository;
import site.keydeuk.store.domain.community.repository.CommunityRepository;
import site.keydeuk.store.domain.image.service.ImageService;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.Community;
import site.keydeuk.store.entity.CommunityImg;
import site.keydeuk.store.entity.User;

import java.util.List;

import static site.keydeuk.store.common.response.ErrorCode.USER_NOT_FOUND;
@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityImgRepository communityImgRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final CommunityCommentService communityCommentService;
    private final CommunityImgService communityImgService;
    private final CommunityLikesService communityLikesService;

    public Community findPostByuserIdAndCommunityId(Long userId, Long CommunityId){
        return communityRepository.findByIdAndUser_Id(CommunityId,userId);
    }

    @Transactional
    public Long createPost(Long userId, PostDto postDto, List<MultipartFile> files){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Community community = Community.builder()
                .user(user)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .viewCount(0)
                .build();
        communityRepository.save(community);

        for (MultipartFile file: files){
            String imgUrl = imageService.uploadCommunityImage(file);
            log.info("imgURl: {}",imgUrl);
            CommunityImg communityImg = CommunityImg.builder()
                    .community(community)
                    .imgUrl(imgUrl)
                    .build();
        communityImgRepository.save(communityImg);
        }
        return community.getId();
    }

    @Transactional
    public void deletePost(Long communityId){
        communityImgService.deleteAllImgByCommunityId(communityId);
        communityLikesService.deleteAllLikesByCommunityId(communityId);
        communityCommentService.deleteAllCommentByCommunityId(communityId);
        communityRepository.deleteById(communityId);

    }
}
