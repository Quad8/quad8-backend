package site.keydeuk.store.test;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserTestResponseDto {

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "비밀번호")
    private String password;

    @Schema(description = "닉네임")
    private String nickname;
}
