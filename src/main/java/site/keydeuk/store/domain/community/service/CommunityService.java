package site.keydeuk.store.domain.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.domain.community.dto.purchase.CustomPurchaseHistoryDto;
import site.keydeuk.store.domain.community.dto.update.UpdatePostDto;
import site.keydeuk.store.domain.community.dto.list.CommunityListResponseDto;
import site.keydeuk.store.domain.community.dto.create.PostDto;
import site.keydeuk.store.domain.community.dto.post.PostResponseDto;
import site.keydeuk.store.domain.community.repository.CommunityImgRepository;
import site.keydeuk.store.domain.community.repository.CommunityRepository;
import site.keydeuk.store.domain.communitycomment.service.CommunityCommentService;
import site.keydeuk.store.domain.communitylikes.service.CommunityLikesService;
import site.keydeuk.store.domain.customoption.repository.CustomObjectRepository;
import site.keydeuk.store.domain.customoption.repository.CustomRepository;
import site.keydeuk.store.domain.image.service.ImageService;
import site.keydeuk.store.domain.order.service.OrderService;
import site.keydeuk.store.domain.user.repository.UserRepository;
import site.keydeuk.store.entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static site.keydeuk.store.common.response.ErrorCode.*;

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
    private final OrderService orderService;

    /** 커스텀키보드 구매내역 확인하기*/
    public List<CustomPurchaseHistoryDto> getPurchaseHistory(Long userId){
        //1. order userid로 조회
        List<Order> orders = orderService.getAllOrdersStatusConfirmedByUserId(userId);

        if (orders.isEmpty()) return Collections.emptyList();

        List<OrderItem> orderItemList = new ArrayList<>();
        //2. order중 1000000번 이상인 것만 조회
        for(Order order : orders){
            log.info("order id: {}",order.getId());

            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems){
                log.info("orderItem id: {}",orderItem.getId());
                if (orderItem.getProductId() > 1000000) orderItemList.add(orderItem);
            }
        }

        List<CustomPurchaseHistoryDto> dtos = new ArrayList<>();

        //3. 리뷰 작성여부 확인해서 dto에 넣기
        if (orderItemList.isEmpty()) return dtos;
        for (OrderItem orderItem : orderItemList){
            CustomOption custom = customRepository.findById(orderItem.getProductId()).orElseThrow(()-> new CustomException(PRODUCT_NOT_FOUND));
            boolean isReviewed = communityRepository.existsByCustomOptionId(custom.getId());
            CustomPurchaseHistoryDto dto;
            if (!custom.isHasPointKey()){ // 포인트키캡 선택X
                dto = new CustomPurchaseHistoryDto(orderItem,custom,isReviewed);
            }else {
                CustomObject object = objectRepository.findById(custom.getId()).orElseThrow(()-> new CustomException(PRODUCT_NOT_FOUND));
                dto = new CustomPurchaseHistoryDto(orderItem,custom,object,isReviewed);
            }
            dtos.add(dto);
        }
        return dtos;
    }

    /** 커뮤니티 전체 게시글 조회 */
    @Transactional(readOnly = true)
    public Page<CommunityListResponseDto> getPostList(String sort, Pageable pageable, Long userId){
        Page<Community> communities = null;
        switch (sort){
            case "popular":
                 communities = communityRepository.findAllOrderByLikes(pageable);
                 break;
            case "views":
                communities = communityRepository.findAllOrderByViewCount(pageable);
                break;
            case "new":
                communities = communityRepository.findAllOrderByUpdatedAt(pageable);
                break;
            default:
                log.error("Invalid sort parameter: {}",sort);
                throw new CustomException(COMMON_INVALID_PARAMETER);
        }

        return communities.map(community -> {
            CommunityListResponseDto dto = new CommunityListResponseDto(community,communityCommentService.getCommentCountByPost(community.getId()));
            dto.setLikeCount(communityLikesService.countByCommunityId(community.getId()));
            if (userId != null){
                dto.setLiked(communityLikesService.existsByUserIdAndCommunityId(userId,community.getId()));
            }
            return dto;
        });
    }

    /** user가 작성한 게시글 조회*/
    public Page<CommunityListResponseDto> getPostsByUserId(String sort, Pageable pageable, Long userId){
        Page<Community> communities;
        switch (sort){
            case "popular":
                communities = communityRepository.findByUserIdOrderByLikes(userId,pageable);
                break;
            case "views":
                communities = communityRepository.findByUserIdOrderByViewCount(userId,pageable);
                break;
            case "new":
                communities = communityRepository.findByUserIdOrderByCreatedAt(userId,pageable);
                break;
            default:
                communities = communityRepository.findByUserId(userId,pageable);
                break;
        }

        return  communities.map(community -> {
            CommunityListResponseDto dto = new CommunityListResponseDto(community,communityCommentService.getCommentCountByPost(community.getId()));
            dto.setLikeCount(communityLikesService.countByCommunityId(community.getId()));
            if (userId != null){
                dto.setLiked(communityLikesService.existsByUserIdAndCommunityId(userId,community.getId()));
            }
            return dto;
        });
    }

    /** 게시글 상세 조회 */
    public PostResponseDto getPostById(Long id){

        Community community = getCommunityById(id);
        CustomOption custom = customRepository.findById(community.getCustomOptionId())
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
        if (custom.isHasPointKey()){
            CustomObject object = objectRepository.findById(community.getCustomOptionId())
                    .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
            // 조회수 증가
            community.setViewCount(community.getViewCount()+1);
            communityRepository.save(community);
            return new PostResponseDto(community,custom,object);
        }else {
            community.setViewCount(community.getViewCount()+1);
            communityRepository.save(community);
            return new PostResponseDto(community,custom);
        }
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

        community.setUpdatedAt(LocalDateTime.now());
        communityRepository.save(community);

        for (MultipartFile file: files){
            String imgUrl = imageService.uploadCommunityImage(file);
            CommunityImg communityImg = CommunityImg.builder()
                    .community(community)
                    .imgUrl(imgUrl)
                    .build();

        communityImgRepository.save(communityImg);
        }
        return community.getId();
    }

    /** 커뮤니티 글 수정하기 */
    @Transactional
    public Long updatePost(Long communityId, UpdatePostDto dto,List<MultipartFile> files){
        Community community = communityRepository.findById(communityId).orElseThrow(()->new CustomException(PRODUCT_NOT_FOUND));

        if (!isImageCountLessThanOrEqualToFour(communityId, dto.getDeletedFileList(),files)) {
            throw new CustomException(IMAGE_MAX_COUNT);
        }

        if (dto.getTitle() != null) community.setTitle(dto.getTitle());
        if ((dto.getContent() != null)) community.setContent(dto.getContent());
        if (dto.getDeletedFileList() != null){
            for (Long id : dto.getDeletedFileList()){
                if (communityImgRepository.findByIdAndCommunity_Id(id, communityId) == null) {
                    throw new CustomException(IMAGE_POST_NOT_MATCH);
                }
                communityImgRepository.deleteById(id);
            }
        }
        community.setUpdatedAt(LocalDateTime.now());
        communityRepository.save(community);
        if (files != null){
            for (MultipartFile file: files){
                String imgUrl = imageService.uploadCommunityImage(file);
                CommunityImg communityImg = CommunityImg.builder()
                        .community(community)
                        .imgUrl(imgUrl)
                        .build();
                communityImgRepository.save(communityImg);
            }
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

    private boolean isImageCountLessThanOrEqualToFour (Long communityId, List<Long> deletedList, List<MultipartFile> files){

        int deletedCount =0;
        int addCount=0;
        if (deletedList != null ) deletedCount = deletedList.size();
        if (files != null ) addCount = files.size();

        Long currentCount = communityImgRepository.countByCommunity_Id(communityId);
        Long afterCount = (currentCount-deletedCount)+addCount;

        log.info("deletedCount: {}, addCount: {}, afterCount:{}",deletedCount, addCount, afterCount);
        if (afterCount>4) return false;
        return true;
    }
}
