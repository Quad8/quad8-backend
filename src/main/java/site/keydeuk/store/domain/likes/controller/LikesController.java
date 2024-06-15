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
        return CommonResponse.ok(LikesResponse.from(like));
    }

    @GetMapping("/products")
    public CommonResponse<List<LikedProductsResponse>> getLikedProducts(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getUserId();
        List<LikedProductsResponse> likedProducts = likesService.getLikedProducts(userId);
        return CommonResponse.ok(likedProducts);
    }
}
