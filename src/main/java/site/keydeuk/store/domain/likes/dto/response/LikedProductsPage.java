package site.keydeuk.store.domain.likes.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
public record LikedProductsPage(
        List<LikedProductsResponse> likedProductsResponses,
        int totalElements, // 전체 요소 갯수
        int totalPages, // 전체 페이지 수
        int currentPage, // 현재 페이지
        boolean first, // 첫번째 페이지인지
        boolean last // 마지막 페이지인지
) {
}
