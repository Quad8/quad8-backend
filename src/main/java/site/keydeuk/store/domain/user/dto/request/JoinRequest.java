package site.keydeuk.store.domain.user.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import site.keydeuk.store.entity.User;
import site.keydeuk.store.entity.enums.Gender;
import site.keydeuk.store.entity.enums.RoleType;

import java.time.LocalDate;

@Schema(description = "회원 가입 요청 데이터")
public record JoinRequest(
        @Schema(description = "사용자 이메일", example = "user@example.com")
        @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "이메일은 유효한 형식이어야 합니다.")
        String email,

        @Schema(description = "사용자 비밀번호", example = "password123")
        @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = 6, max = 20, message = "비밀번호는 6자 이상, 20자 이하여야 합니다.")
        String password,

        @Schema(description = "사용자 생년월일", example = "1990-01-01")
        @NotNull(message = "생년월일은 필수 입력 항목입니다.")
        LocalDate birth,

        @Schema(description = "사용자 휴대폰 번호", example = "01012345678")
        @NotEmpty(message = "휴대폰 번호는 필수 입력 항목입니다.")
        @Size(min = 11, max = 11, message = "휴대폰 번호는 11자의 숫자여야 합니다.")
        String phone,

        @Schema(description = "사용자 성별", example = "MALE")
        @NotNull(message = "성별은 필수 입력 항목입니다.")
        Gender gender,

        @Schema(description = "사용자 닉네임", example = "nickname123")
        @NotEmpty(message = "닉네임은 필수 입력 항목입니다.")
        @Size(max = 15, message = "닉네임은 1글자 이상, 15자 이하여야 합니다.")
        String nickname,

        @Schema(description = "사용자 프로필 이미지 URL", example = "http://example.com/image.jpg")
        String imgUrl,

        @Schema(description = "OAuth2 제공자", example = "GOOGLE")
        String provider,

        @Schema(description = "OAuth2 제공자 ID", example = "1234567890")
        String providerId
) {
    public User toEntity(String encodePassword) {
        return User.builder()
                .email(email)
                .password(encodePassword)
                .birth(birth)
                .phone(phone)
                .nickname(nickname)
                .gender(gender)
                .imgUrl(imgUrl)
                .provider(provider)
                .providerId(providerId)
                .role(RoleType.ROLE_USER)
                .build();
    }
}