package site.keydeuk.store.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.domain.review.dto.request.CreateReviewRequest;
import site.keydeuk.store.domain.review.service.ReviewService;
import site.keydeuk.store.domain.security.PrincipalDetails;

import java.util.List;

@Tag(name = "Review", description = "리뷰 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @Operation(summary = "리뷰 작성", description = "리뷰를 작성합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<Long> createReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("productId") Integer productId,
            @RequestPart("createReviewRequest") @Validated CreateReviewRequest createReviewRequest,
            @RequestPart(value = "reviewImgs", required = false) List<MultipartFile> reviewImgs
    ) {
        Long userId = principalDetails.getUserId();
        Long reviewId = reviewService.createReview(userId, productId, createReviewRequest, reviewImgs);
        return CommonResponse.ok(reviewId);
    }

}
