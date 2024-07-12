package site.keydeuk.store.domain.likes.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
public record LikedProductsPage(
        List<LikedProductsResponse> likedProductsResponses,
        Pageable pageable
) {
}
