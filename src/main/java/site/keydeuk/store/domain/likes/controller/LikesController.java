package site.keydeuk.store.domain.likes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.likes.dto.response.LikedProductsResponse;
import site.keydeuk.store.domain.likes.dto.response.LikesResponse;
import site.keydeuk.store.domain.likes.service.LikesService;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.entity.Likes;
import site.keydeuk.store.entity.Product;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/{productId}")
    public CommonResponse<LikesResponse> addLikes(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Integer productId
    ) {
        Likes like = likesService.addLikes(principalDetails.getUserId(), productId);
        return CommonResponse.ok("좋아요가 등록되었습니다.",LikesResponse.from(like));
    }

    @DeleteMapping("/{productId}")
    public CommonResponse<Integer> deleteLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Integer productId
    ) {
        Long userId = principalDetails.getUserId();
        likesService.deleteLike(userId, productId);
        return CommonResponse.ok("좋아요가 삭제되었습니다.", productId);
    }

    @GetMapping("/products")
    public CommonResponse<List<LikedProductsResponse>> getLikedProducts(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getUserId();
        List<LikedProductsResponse> likedProducts = likesService.getLikedProducts(userId);
        return CommonResponse.ok(likedProducts);
    }
}
