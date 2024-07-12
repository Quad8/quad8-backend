package site.keydeuk.store.domain.review.dto.response;

import lombok.Builder;
import site.keydeuk.store.domain.review.dto.ReviewDto;

import java.util.List;
import java.util.Map;

@Builder
public record ReviewResponse(
        List<ReviewDto> reviewDtoList,
        Double averageScore,
        Map<String, Map<Integer, Double>> reviewStatistics,
        Long totalElements,
        int totalPages,
        int currentPage,
        boolean first,
        boolean last
) {
    public static ReviewResponse of(List<ReviewDto> reviewDtoList,
                                    Double averageScore,
                                    Long reviewCounts,
                                    Map<String, Map<Integer, Double>> reviewStatistics,
                                    int totalPages,
                                    int currentPage,
                                    boolean isFirst,
                                    boolean isLast
    ) {
        return ReviewResponse.builder()
                .reviewDtoList(reviewDtoList)
                .averageScore(averageScore)
                .reviewStatistics(reviewStatistics)
                .totalElements(reviewCounts)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .first(isFirst)
                .last(isLast)
                .build();
    }
}
