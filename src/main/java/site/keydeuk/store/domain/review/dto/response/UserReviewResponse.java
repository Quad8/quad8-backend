package site.keydeuk.store.domain.review.dto.response;

import lombok.Builder;
import site.keydeuk.store.domain.review.dto.ReviewDto;

import java.util.List;
import java.util.Map;

@Builder
public record UserReviewResponse(
        List<ReviewDto> reviewDtoList,
        Long totalElements,
        int totalPages,
        int currentPage,
        boolean first,
        boolean last
) {
    public static UserReviewResponse of(List<ReviewDto> reviewDtoList,
                                        Long reviewCounts,
                                        int totalPages,
                                        int currentPage,
                                        boolean isFirst,
                                        boolean isLast
    ) {
        return UserReviewResponse.builder()
                .reviewDtoList(reviewDtoList)
                .totalElements(reviewCounts)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .first(isFirst)
                .last(isLast)
                .build();
    }
}