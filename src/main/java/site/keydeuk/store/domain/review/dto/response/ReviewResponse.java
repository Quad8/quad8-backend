package site.keydeuk.store.domain.review.dto.response;

import lombok.Builder;
import site.keydeuk.store.domain.review.dto.ReviewDto;

import java.util.List;
import java.util.Map;

@Builder
public record ReviewResponse(
        List<ReviewDto> reviewDtoList,
        Double averageScore,
        Long reviewCounts,
        Map<String, Map<Integer, Double>> reviewStatistics
) {
    public static ReviewResponse of(List<ReviewDto> reviewDtoList,
                                    Double averageScore,
                                    Long reviewCounts,
                                    Map<String, Map<Integer, Double>> reviewStatistics
    ) {
        return ReviewResponse.builder()
                .reviewDtoList(reviewDtoList)
                .averageScore(averageScore)
                .reviewCounts(reviewCounts)
                .reviewStatistics(reviewStatistics)
                .build();
    }
}
