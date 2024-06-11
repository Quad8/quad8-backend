package site.keydeuk.store.domain.user.dto.response;

import lombok.Builder;
import site.keydeuk.store.entity.User;
import site.keydeuk.store.entity.enums.Gender;

import java.time.LocalDate;

@Builder
public record UserResponse(
        Long id,
        String email,
        String nickname,
        LocalDate birth,
        String phone,
        Gender gender,
        String imgUrl
) {
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .birth(user.getBirth())
                .phone(user.getPhone())
                .gender(user.getGender())
                .imgUrl(user.getImgUrl())
                .build();
    }
}
