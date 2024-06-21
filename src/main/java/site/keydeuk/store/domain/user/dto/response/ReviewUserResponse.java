package site.keydeuk.store.domain.user.dto.response;

import lombok.Builder;
import site.keydeuk.store.entity.User;

@Builder
public record ReviewUserResponse(
        Long id,
        String nickname,
        String imgUrl
) {
    public static ReviewUserResponse from(User user) {
        return ReviewUserResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .imgUrl(user.getImgUrl())
                .build();
    }
}
