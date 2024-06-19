package site.keydeuk.store.domain.review.dto.response;

import lombok.Builder;
import site.keydeuk.store.entity.Review;
import site.keydeuk.store.entity.ReviewImg;

import java.util.List;

@Builder
public record ReviewResponse(
        Long id,
        String content,
        Double score,
        Integer option1,
        Integer option2,
        Integer option3,
        List<String> reviewImageUrls,
        Long userId,
        Integer productId
) {
    public static ReviewResponse from(Review review) {
        List<String> imageUrls = review.getReviewImages().stream()
                .map(ReviewImg::getImgUrl)
                .toList();

        return ReviewResponse.builder()
                .id(review.getId())
                .content(review.getContent())
                .score(review.getScore())
                .option1(review.getOption1())
                .option2(review.getOption2())
                .option3(review.getOption3())
                .reviewImageUrls(imageUrls)
                .userId(review.getUser().getId())
                .productId(review.getProduct().getId())
                .build();
    }
}