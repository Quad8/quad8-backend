package site.keydeuk.store.domain.review.dto;

import lombok.Builder;
import site.keydeuk.store.domain.user.dto.response.ReviewUserResponse;
import site.keydeuk.store.entity.Review;
import site.keydeuk.store.entity.ReviewImg;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReviewDto(
        Long id,
        Long orderId,
        String switchOption,
        ReviewUserResponse writer,
        String content,
        Integer score,
        Integer option1,
        Integer option2,
        Integer option3,
        List<ReviewImgDto> reviewImgs,
        Long likeCount,
        Boolean likedByUser,
        Integer productId,
        LocalDateTime updatedAt
) {
    public static ReviewDto of(Review review,String switchOption, ReviewUserResponse writer, Long likeCount, Boolean likedByUser) {
        List<ReviewImgDto> reviewImgs = getReviewImgDtos(review);

        return ReviewDto.builder()
                .id(review.getId())
                .orderId(review.getOrderId())
                .switchOption(switchOption)
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