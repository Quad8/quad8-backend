package site.keydeuk.store.domain.likes.dto.response;

import lombok.Builder;

@Builder
public record LikedProductsResponse(
        Integer productId,
        String productImg,
        String productName,
        Long price
) {
}
