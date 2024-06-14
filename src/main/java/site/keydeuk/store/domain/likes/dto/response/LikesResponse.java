package site.keydeuk.store.domain.likes.dto.response;

import lombok.Builder;
import site.keydeuk.store.entity.Likes;

@Builder
public record LikesResponse(
        Long id,
        Long userId,
        Integer productId
) {
    public static LikesResponse from(Likes likes) {
        return LikesResponse.builder()
                .id(likes.getId())
                .userId(likes.getUser().getId())
                .productId(likes.getProduct().getId())
                .build();
    }
}
