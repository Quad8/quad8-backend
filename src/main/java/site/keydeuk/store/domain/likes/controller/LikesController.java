package site.keydeuk.store.domain.likes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.likes.dto.response.LikesResponse;
import site.keydeuk.store.domain.likes.service.LikesService;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.entity.Likes;

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
}
