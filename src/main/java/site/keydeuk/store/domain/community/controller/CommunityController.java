package site.keydeuk.store.domain.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.community.dto.list.CommunityListResponseDto;
import site.keydeuk.store.domain.community.dto.update.UpdatePostDto;
import site.keydeuk.store.domain.community.dto.list.CommunityListRequestDto;
import site.keydeuk.store.domain.community.dto.create.PostDto;
import site.keydeuk.store.domain.community.dto.post.PostResponseDto;
import site.keydeuk.store.domain.community.service.CommunityService;
import site.keydeuk.store.domain.communitycomment.dto.CommentResponseDto;
import site.keydeuk.store.domain.communitycomment.service.CommunityCommentService;
import site.keydeuk.store.domain.communitylikes.service.CommunityLikesService;
import site.keydeuk.store.domain.customoption.service.CustomService;
import site.keydeuk.store.domain.security.PrincipalDetails;

import java.util.List;
import site.keydeuk.store.entity.Community;

import static site.keydeuk.store.common.response.ErrorCode.INVALID_PAGEABLE_PAGE;

@Slf4j
@Tag(name ="Community", description = "커뮤니티 관련 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/community")
@RestController
public class CommunityController {

    private final CommunityService communityService;
    private final CommunityCommentService commentService;
    private final CommunityLikesService communityLikesService;
    private final CustomService customService;
    private final CommunityCommentService communityCommentService;

    @Operation(summary = "커스텀키보드 구매내역 조회", description = "커스텀 키보드 구매내역을 조회합니다.")
    @GetMapping("/purchase-history")
    public CommonResponse<?> getPurchaseHistory(@AuthenticationPrincipal PrincipalDetails principalDetails){

        return CommonResponse.ok(communityService.getPurchaseHistory(principalDetails.getUserId()));
    }

    @Operation(summary = "커뮤니티 전체 조회", description = "커뮤니티 전체 글 목록을 조회합니다.")
    @Parameter(name = "sort", description = "new : 최신순, views : 조회순, popular : 인기순", example = "new")
    @GetMapping("/all")
    public CommonResponse<?> getAllPostList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @ParameterObject @Valid CommunityListRequestDto requestDto){
        Long userId = null;
        if (principalDetails!= null){
            userId = principalDetails.getUserId();
        }
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());
        Page<CommunityListResponseDto> page = communityService.getPostList(requestDto.getSort(),pageable,userId);

        if (page.isEmpty()) return CommonResponse.ok("조회된 게시글이 없습니다.",page);
        if (page.getTotalPages() <= requestDto.getPage()) throw new CustomException(INVALID_PAGEABLE_PAGE);

        return CommonResponse.ok(page);
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 Id로 상세 정보를 조회합니다.")
    @Parameter(name = "id", description = "게시글 ID", example = "11")
    @GetMapping("/{id}")
    public CommonResponse<?> getPostDetailById(@PathVariable("id") Long id,@AuthenticationPrincipal PrincipalDetails principalDetails){

        PostResponseDto dto = communityService.getPostById(id);
        Pageable pageable = PageRequest.of(0,5);
        List<CommentResponseDto> comments = commentService.getCommentListByPost(dto.getId(),pageable);
        dto.setComments(comments);
        dto.setCommentCount(communityCommentService.getCommentCountByPost(id));
        dto.setLikeCount(communityLikesService.countByCommunityId(id));

        if (principalDetails != null){
            Long userId = principalDetails.getUserId();
            boolean isLiked = communityLikesService.existsByUserIdAndCommunityId(userId,id);
            dto.setLiked(isLiked);
        }
        return CommonResponse.ok(dto);
    }

    @Operation(summary = "커뮤니티에 작성한 내 글 조회", description = "마이페이지에서 커뮤니티에 작성한 글 조회")
    @GetMapping("/user")
    public CommonResponse<?> getPostsByUserId(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @ParameterObject @Valid CommunityListRequestDto requestDto){

        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());

        Page<CommunityListResponseDto> page = communityService.getPostsByUserId(requestDto.getSort(),pageable, principalDetails.getUserId());

        if (page.isEmpty()) return CommonResponse.ok("조회된 게시글이 없습니다.",page);

        if (page.getTotalPages() <= requestDto.getPage()) throw new CustomException(INVALID_PAGEABLE_PAGE);

        return CommonResponse.ok(page);
    }

    @Operation(summary = "커뮤니티 글 작성하기", description = "커스텀 키보드 구매 내역 확인 후 글 작성이 가능합니다. ")
    @PostMapping("/create")
    public CommonResponse<?> createPost(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @Validated @RequestPart("postDto")PostDto postDto,
                                        @RequestPart(value = "files")@Parameter(description = "이미지 파일")List<MultipartFile> files){
        if (communityService.existPostBycustomId(postDto.getProductId())) return CommonResponse.error("해당 구매내역에 게시글이 존재합니다.");
        if (!customService.existBycustomId(postDto.getProductId())) return CommonResponse.error("해당 커스텀키보드 ID가 존재하지 않습니다.");

        if (files.size() >4){
            return CommonResponse.error("파일은 최대 4장까지 가능합니다.");
        }
        return CommonResponse.ok("글 저장되었습니다.",communityService.createPost(principalDetails.getUserId(),postDto,files));
    }

    @Operation(summary = "커뮤니티 글 수정하기", description = "작성한 글을 수정합니다.")
    @PutMapping("/update/{id}")
    public CommonResponse<?> updatePost(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @PathVariable("id")Long id,
                                        @Validated @RequestPart("postDto") UpdatePostDto dto,
                                        @RequestPart(value = "files",required = false)@Parameter(description = "이미지 파일") List<MultipartFile> files){

        if (communityService.findPostByuserIdAndCommunityId(principalDetails.getUserId(), id) == null) return CommonResponse.error("작성한 게시글이 아닙니다.");

        return CommonResponse.ok("게시글이 수정되었습니다.",communityService.updatePost(id,dto,files));
    }

    @Operation(summary = "커뮤니티 글 삭제하기", description = "작성된 글을 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public CommonResponse<?> deletePost(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("id")Long id){
        // user가 쓴 게시글 맞는지 확인
        Community community = communityService.findPostByuserIdAndCommunityId(principalDetails.getUserId(), id);
        if (community == null) return CommonResponse.error("작성한 게시글이 아닙니다.");

        communityService.deletePost(community);

        return CommonResponse.ok("게시글이 삭제되었습니다.");
    }

}
