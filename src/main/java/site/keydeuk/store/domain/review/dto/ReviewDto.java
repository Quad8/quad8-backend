package site.keydeuk.store.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import site.keydeuk.store.domain.user.dto.response.ReviewUserResponse;
import site.keydeuk.store.entity.Review;
import site.keydeuk.store.entity.ReviewImg;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReviewDto(
        @Schema(description = "리뷰 ID", example = "1")
        Long id,

        @Schema(description = "주문 ID", example = "12345")
        Long orderId,

        @Schema(description = "스위치 옵션", example = "옵션 이름")
        String switchOption,

        @Schema(description = "제품 카테고리 ID", example = "3")
        Integer productCategoryId,

        @Schema(description = "작성자 정보")
        ReviewUserResponse writer,

        @Schema(description = "리뷰 내용", example = "이 제품은 정말 좋습니다!")
        String content,

        @Schema(description = "리뷰 점수", example = "5")
        Integer score,

        @Schema(description = "옵션 1", example = "1")
        Integer option1,

        @Schema(description = "옵션 2", example = "2")
        Integer option2,

        @Schema(description = "옵션 3", example = "3")
        Integer option3,

        @Schema(description = "리뷰 이미지 목록")
        List<ReviewImgDto> reviewImgs,

        @Schema(description = "좋아요 수", example = "100")
        Long likeCount,

        @Schema(description = "사용자가 좋아요를 눌렀는지 여부", example = "true")
        Boolean likedByUser,

        @Schema(description = "제품 ID", example = "50")
        Integer productId,

        @Schema(description = "리뷰 수정 날짜", example = "2023-01-01T00:00:00")
        LocalDateTime updatedAt
) {
    public static ReviewDto of(Review review,String switchOption, ReviewUserResponse writer, Long likeCount, Boolean likedByUser) {
        List<ReviewImgDto> reviewImgs = getReviewImgDtos(review);

        return ReviewDto.builder()
                .id(review.getId())
                .orderId(review.getOrderId())
                .switchOption(switchOption)
                .productCategoryId(review.getProduct().getCategoryId())
                .writer(writer)
                .content(review.getContent())
                .score(review.getScore())
                .option1(review.getOption1())
                .option2(review.getOption2())
                .option3(review.getOption3())
                .reviewImgs(reviewImgs)
                .likeCount(likeCount)
                .likedByUser(likedByUser)
                .productId(review.getProduct().getId())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    public static ReviewDto from(Review review) {
        List<ReviewImgDto> reviewImgs = getReviewImgDtos(review);

        return ReviewDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .score(review.getScore())
                .option1(review.getOption1())
                .option2(review.getOption2())
                .option3(review.getOption3())
                .reviewImgs(reviewImgs)
                .productId(review.getProduct().getId())
                .build();
    }

    private static List<ReviewImgDto> getReviewImgDtos(Review review) {
        return review.getReviewImages().stream()
                .map(ReviewImgDto::from)
                .toList();
    }
}