package site.keydeuk.store.domain.communitylikes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.communitylikes.dto.CommunityLikeDto;
import site.keydeuk.store.domain.communitylikes.service.CommunityLikesService;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.entity.CommunityLikes;

@Slf4j
@Tag(name ="CommunityLikes", description = "커뮤니티 좋아요 관련 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/likes")
@RestController
public class CommunityLikesController {

    private final CommunityLikesService likesService;

    @Operation(summary = "커뮤니티 좋아요 등록", description = "특정 게시글에 대해 좋아요를 등록합니다.")
    @PostMapping("/{communityId}")
    public CommonResponse<?> addLikes(
            @AuthenticationPrincipal @Parameter PrincipalDetails principalDetails,
            @PathVariable @Parameter(description = "좋아요를 추가할 게시글의 ID", required = true) Long communityId
    ) {
        CommunityLikes likes = likesService.addLikes(principalDetails.getUserId(),communityId);
        return CommonResponse.ok("좋아요가 등록되었습니다.",new CommunityLikeDto(likes));
    }

    @Operation(summary = "커뮤니티 좋아요 삭제", description = "특정 게시글에 대해 좋아요를 삭제합니다.")
    @DeleteMapping("/{communityId}")
    public CommonResponse<?> deleteLikes(
            @AuthenticationPrincipal @Parameter PrincipalDetails principalDetails,
            @PathVariable @Parameter(description = "좋아요를 삭제할 게시글의 ID", required = true) Long communityId
    ) {
        likesService.deleteLike(principalDetails.getUserId(),communityId);
        return CommonResponse.ok("좋아요가 삭제되었습니다.",communityId);
    }

}
