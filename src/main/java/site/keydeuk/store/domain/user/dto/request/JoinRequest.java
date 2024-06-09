package site.keydeuk.store.domain.user.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import site.keydeuk.store.entity.User;
import site.keydeuk.store.entity.enums.Gender;
import site.keydeuk.store.entity.enums.RoleType;

import java.time.LocalDate;

public record JoinRequest(
        @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "이메일은 유효한 형식이어야 합니다.")
        String email,
        @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = 6, max = 20, message = "비밀번호는 6자 이상, 20자 이하여야 합니다.")
        String password,
        @NotNull
        LocalDate birth,
        @NotEmpty(message = "휴대폰 번호는 필수 입력 항목입니다.")
        @Size(min = 11, max = 11, message = "휴대폰 번호는 11자의 숫자여야 합니다.")
        String phone,
        @NotNull
        Gender gender,
        @NotEmpty(message = "닉네임은 필수 입력 항목입니다.")
        @Size(max = 15, message = "닉네임은 1글자 이상, 15자 이하여야 합니다.")
        String nickname,
        String imgUrl,
        String provider,
        String providerId
) {

    public User toEntity(String encodePassword) {
        return User.builder()
                .email(email)
                .password(encodePassword)
                .birth(birth)
                .phone(phone)
                .nickname(nickname)
                .imgUrl(imgUrl)
                .provider(provider)
                .providerId(providerId)
                .role(RoleType.ROLE_USER)
                .build();
    }
}