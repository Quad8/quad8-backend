package site.keydeuk.store.domain.review.dto.response;

import lombok.Builder;
import site.keydeuk.store.domain.review.dto.ReviewDto;

import java.util.List;
import java.util.Map;

@Builder
public record UserReviewResponse(
        List<ReviewDto> reviewDtoList,
        Long reviewCounts,
        int totalPages,
        boolean isFirst
) {
    public static UserReviewResponse of(List<ReviewDto> reviewDtoList,
                                    Long reviewCounts,
                                    int totalPages,
                                    boolean isFirst
    ) {
        return UserReviewResponse.builder()
                .reviewDtoList(reviewDtoList)
                .reviewCounts(reviewCounts)
                .totalPages(totalPages)
                .isFirst(isFirst)
                .build();
    }
}