package site.keydeuk.store.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewListRequest {
    @NotNull
    @Schema(description = "정렬(인기순 : likes, 최신순 : createdAt, 평점높은순: scoreHigh, 평점낮은순: scoreLow)", example = "createdAt")
    private String sort;

    @Schema(description = "현재 페이지, default 0", example = "0")
    private int page;

    @Schema(description = "개수, default 10", example = "10")
    private int size;

    @Schema(description = "리뷰 조회 시작 날짜", example = "2023-01-01T00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "리뷰 조회 종료 날짜", example = "2023-12-31T23:59:59")
    private LocalDateTime endDate;

    @Override
    public String toString() {
        return "ReviewListRequest{" +
                "sort='" + sort + '\'' +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
