package site.keydeuk.store.domain.likes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.likes.dto.response.LikedProductsResponse;
import site.keydeuk.store.domain.likes.dto.response.LikesResponse;
import site.keydeuk.store.domain.likes.service.LikesService;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.entity.Likes;

import java.util.List;

@Tag(name = "Likes", description = "좋아요 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikesController {
    private final LikesService likesService;

    @Operation(summary = "좋아요 등록", description = "특정 상품에 대해 좋아요를 등록합니다.")
    @PostMapping("/{productId}")
    public CommonResponse<LikesResponse> addLikes(
            @AuthenticationPrincipal @Parameter PrincipalDetails principalDetails,
            @PathVariable @Parameter(description = "좋아요를 추가할 상품의 ID", required = true) Integer productId
    ) {
        Likes like = likesService.addLikes(principalDetails.getUserId(), productId);
        return CommonResponse.ok("좋아요가 등록되었습니다.",LikesResponse.from(like));
    }

    @Operation(summary = "좋아요 삭제", description = "특정 상품에 대해 좋아요를 삭제합니다.")
    @DeleteMapping("/{productId}")
    public CommonResponse<Integer> deleteLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable @Parameter(description = "좋아요를 삭제할 상품의 ID", required = true) Integer productId
    ) {
        Long userId = principalDetails.getUserId();
        likesService.deleteLike(userId, productId);
        return CommonResponse.ok("좋아요가 삭제되었습니다.", productId);
    }

    @Operation(summary = "좋아요한 상품 조회", description = "사용자가 좋아요한 모든 상품을 조회합니다.")
    @GetMapping("/products")
    public CommonResponse<List<LikedProductsResponse>> getLikedProducts(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getUserId();
        List<LikedProductsResponse> likedProducts = likesService.getLikedProducts(userId);
        return CommonResponse.ok(likedProducts);
    }
}
