package site.keydeuk.store.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewListRequest {
    @NotNull
    @Schema(description = "정렬(인기순 : likes, 최신순 : createdAt)", example = "createdAt")
    private String sort;

    @Schema(description = "현재 페이지, default 0", example = "0")
    private int page;

    @Schema(description = "개수, default 10", example = "10")
    private int size;

    @Override
    public String toString() {
        return "ReviewListRequest{" +
                "sort='" + sort + '\'' +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
