package site.keydeuk.store.domain.likes.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
public record LikedProductsPage(
        @Schema(description = "좋아요한 상품 목록")
        List<LikedProductsResponse> likedProductsResponses,

        @Schema(description = "전체 요소 개수", example = "100")
        int totalElements,

        @Schema(description = "전체 페이지 수", example = "10")
        int totalPages,

        @Schema(description = "현재 페이지", example = "1")
        int currentPage,

        @Schema(description = "첫번째 페이지 여부", example = "true")
        boolean first,

        @Schema(description = "마지막 페이지 여부", example = "false")
        boolean last
) {
}
