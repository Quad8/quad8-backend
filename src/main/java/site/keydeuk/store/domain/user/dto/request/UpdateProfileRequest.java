package site.keydeuk.store.domain.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import site.keydeuk.store.entity.enums.Gender;

public record UpdateProfileRequest(
        String imgUrl,
        @NotEmpty(message = "닉네임은 필수 입력 항목입니다.")
        @Size(max = 15, message = "닉네임은 1글자 이상, 15자 이하여야 합니다.")
        String nickname,
        Gender gender,
        @NotEmpty(message = "휴대폰 번호는 필수 입력 항목입니다.")
        @Size(min = 11, max = 11, message = "휴대폰 번호는 11자의 숫자여야 합니다.")
        String phone
) {
}
