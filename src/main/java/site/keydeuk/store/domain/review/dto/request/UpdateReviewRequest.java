package site.keydeuk.store.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import site.keydeuk.store.domain.review.dto.ReviewImgDto;
import site.keydeuk.store.entity.Review;

import java.util.List;

public record UpdateReviewRequest(
        @Schema(description = "리뷰 내용", example = "Great product!")
        @NotEmpty(message = "리뷰 내용은 필수 입력 항목입니다.")
        String content,

        @Schema(description = "리뷰 점수", example = "4.5")
        @NotNull(message = "리뷰 점수는 필수 입력 항목입니다.")
        Integer score,

        @Schema(description = "옵션1", example = "1")
        Integer option1,

        @Schema(description = "옵션2", example = "1")
        Integer option2,

        @Schema(description = "옵션3", example = "1")
        Integer option3,

        @Schema(description = "기존 리뷰 이미지 목록")
        List<ReviewImgDto> existingReviewImgs
) {
    public Review toEntity(Review review) {
        review.update(content, score, option1, option2, option3);
        return review;
    }
}