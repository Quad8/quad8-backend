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
import site.keydeuk.store.domain.review.dto.ReviewDto;
import site.keydeuk.store.domain.review.dto.request.CreateReviewRequest;
import site.keydeuk.store.domain.review.dto.request.UpdateReviewRequest;
import site.keydeuk.store.domain.review.dto.response.ReviewResponse;
import site.keydeuk.store.domain.review.service.ReviewService;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.entity.Review;

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

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    public CommonResponse<Void> deleteReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long reviewId
    ) {
        Long userId = principalDetails.getUserId();
        reviewService.deleteReview(userId, reviewId);
        return CommonResponse.ok();
    }

    @GetMapping("/user")
    @Operation(summary = "사용자 리뷰 조회", description = "사용자가 작성한 모든 리뷰를 조회합니다.")
    public CommonResponse<List<ReviewDto>> getUserReviews(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getUserId();
        List<Review> userReviews = reviewService.getUserReviews(userId);
        List<ReviewDto> response = userReviews.stream()
                .map(ReviewDto::from)
                .toList();
        return CommonResponse.ok(response);
    }

    @GetMapping()
    @Operation(summary = "제품 리뷰 조회", description = "특정 제품의 모든 리뷰를 조회합니다.")
    public CommonResponse<ReviewResponse> getProductReviews(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("productId") Integer productId) {
        Long userId = principalDetails != null ? principalDetails.getUserId() : null;
        ReviewResponse response = reviewService.getProductReviews(productId, userId);
        return CommonResponse.ok(response);
    }

    @PutMapping(value = "/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    public CommonResponse<Long> updateReview(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long reviewId,
            @RequestPart("createReviewRequest") @Validated UpdateReviewRequest updateReviewRequest,
            @RequestPart(value = "reviewImgs", required = false) List<MultipartFile> reviewImgs
    ) {
        Long userId = principalDetails.getUserId();
        Long updatedReviewId = reviewService.updateReview(userId, reviewId, updateReviewRequest, reviewImgs);
        return CommonResponse.ok(updatedReviewId);
    }
}
