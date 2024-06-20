package site.keydeuk.store.domain.community.service;

import jakarta.persistence.EntityNotFoundException;
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
import site.keydeuk.store.domain.community.dto.list.CommunityListResponseDto;
import site.keydeuk.store.domain.community.dto.create.PostDto;
import site.keydeuk.store.domain.community.dto.post.PostResponseDto;
import site.keydeuk.store.domain.community.repository.CommunityImgRepository;
import site.keydeuk.store.domain.community.repository.CommunityRepository;
import site.keydeuk.store.domain.customoption.repository.CustomObjectRepository;
import site.keydeuk.store.domain.customoption.repository.CustomRepository;
import site.keydeuk.store.domain.image.service.ImageService;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.*;

import java.util.List;
import java.util.NoSuchElementException;

import static site.keydeuk.store.common.response.ErrorCode.PRODUCT_NOT_FOUND;
import static site.keydeuk.store.common.response.ErrorCode.USER_NOT_FOUND;
@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityImgRepository communityImgRepository;
    private final UserRepository userRepository;
    private final CustomObjectRepository objectRepository;
    private final CustomRepository customRepository;
    private final ImageService imageService;
    private final CommunityCommentService communityCommentService;
    private final CommunityImgService communityImgService;
    private final CommunityLikesService communityLikesService;

    /** 커뮤니티 전체 게시글 조회 */
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

    /** user가 작성한 게시글 조회*/
    public Page<CommunityListResponseDto> getPostsByUserId(String sort, Pageable pageable, Long userId){
        switch (sort){
            // case "popular": 인기순
            case "views":
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("viewCount").descending());
                break;
            case "createdAt_desc":
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
                break;
        }
        Page<Community> communityList = communityRepository.findByUserId(userId,pageable);

        return communityList.map(CommunityListResponseDto::new);
    }

    /** 게시글 상세 조회 */
    public PostResponseDto getPostById(Long id){

        Community community = getCommunityById(id);
        CustomObject object = objectRepository.findById(community.getCustomOptionId())
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
        CustomOption custom = customRepository.findById(community.getCustomOptionId())
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
        return new PostResponseDto(community,custom,object);
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
                .customOptionId(postDto.getProductId())
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

    public Community findPostByuserIdAndCommunityId(Long userId, Long CommunityId){
        return communityRepository.findByIdAndUser_Id(CommunityId,userId);
    }

    public boolean existPostBycustomId(Integer id){
        boolean flag = false;
        if (communityRepository.findByCustomOptionId(id) != null) flag = true;
        return flag;
    }

    private Community getCommunityById(Long id){
        return communityRepository.findById(id).orElseThrow(
                ()-> new NoSuchElementException("Community Post not found with id: " + id)
        );
    }
}
