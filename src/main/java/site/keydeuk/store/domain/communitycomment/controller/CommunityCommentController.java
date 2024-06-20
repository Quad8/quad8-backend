package site.keydeuk.store.domain.communitycomment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.communitycomment.dto.create.CommentDto;
import site.keydeuk.store.domain.communitycomment.service.CommunityCommentService;
import site.keydeuk.store.domain.security.PrincipalDetails;

@Tag(name = "CommunityComment", description = "커뮤니티 댓글 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/comment")
public class CommunityCommentController {

    private final CommunityCommentService commentService;
    
    @Operation(summary = "커뮤니티 게시글 댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @PostMapping("/{communityId}")
    public CommonResponse<?> addComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @PathVariable @Parameter(description = "댓글 등록할 게시글의 ID", required = true) Long communityId,
                                        @RequestBody CommentDto dto){

        return CommonResponse.ok(commentService.addComment(principalDetails.getUserId(),communityId,dto));
    }

    @Operation(summary = "커뮤니티 게시글 댓글 삭제", description = "유저가 작성한 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public CommonResponse<?> deleteComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @PathVariable @Parameter(description = "삭제할 댓글 ID", required = true) Long commentId){
        commentService.deleteComment(principalDetails.getUserId(), commentId);
        return CommonResponse.ok("댓글이 삭제되었습니다.");
    }
}
