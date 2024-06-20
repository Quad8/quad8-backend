package site.keydeuk.store.domain.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.community.dto.communitylist.CommunityListResponseDto;
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

    @Transactional(readOnly = true)
    public Page<CommunityListResponseDto> getPostList(String sort, Pageable pageable, Long userId){
        Page<Community> communities;
        switch (sort){
            // case "popular": 인기순
            case "views":
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("viewCount").descending());
                break;
            case "createdAt_desc":
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
                break;
        }
        communities = communityRepository.findAll(pageable);

        return  communities.map(community -> {
            boolean isLiked = false;
            if (userId != null){
                isLiked = true; // 재구현 필요
            }
            return new CommunityListResponseDto(community);
        });
    }
    public Page<Community> getAllCommunities(Pageable pageable) {
        return communityRepository.findAll(pageable);
    }
    public Community findPostByuserIdAndCommunityId(Long userId, Long CommunityId){
        return communityRepository.findByIdAndUser_Id(CommunityId,userId);
    }

    /**커뮤니티 글 작성하기*/
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

    /** 커뮤니티 글 삭제하기*/
    @Transactional
    public void deletePost(Long communityId){
        communityImgService.deleteAllImgByCommunityId(communityId);
        communityLikesService.deleteAllLikesByCommunityId(communityId);
        communityCommentService.deleteAllCommentByCommunityId(communityId);
        communityRepository.deleteById(communityId);

    }
}
