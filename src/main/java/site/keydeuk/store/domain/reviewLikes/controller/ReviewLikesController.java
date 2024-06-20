package site.keydeuk.store.domain.reviewLikes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.reviewLikes.service.ReviewLikesService;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.entity.ReviewLikes;

@Tag(name = "ReviewLikes", description = "리뷰 좋아요 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews/likes")
public class ReviewLikesController {
    private final ReviewLikesService reviewLikesService;

    @Operation(summary = "리뷰 좋아요 등록", description = "특정 상품 리뷰에 대해 좋아요를 등록합니다.")
    @PostMapping("/{reviewId}")
    public CommonResponse<Long> addLike(
            @AuthenticationPrincipal @Parameter PrincipalDetails principalDetails,
            @PathVariable @Parameter(description = "좋아요를 추가할 리뷰의 ID", required = true) Long reviewId
    ) {
        ReviewLikes reviewLike = reviewLikesService.addLikes(principalDetails.getUserId(), reviewId);
        return CommonResponse.ok("리뷰 좋아요가 등록되었습니다.", reviewLike.getId());
    }

    @Operation(summary = "리뷰 좋아요 삭제", description = "특정 리뷰에 대해 좋아요를 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    public CommonResponse<Long> deleteLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable @Parameter(description = "좋아요를 삭제할 상품의 ID", required = true) Long reviewId
    ) {
        reviewLikesService.deleteLike(principalDetails.getUserId(), reviewId);
        return CommonResponse.ok("좋아요가 삭제되었습니다.", reviewId);
    }
}
