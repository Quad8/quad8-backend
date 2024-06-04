package site.keydeuk.store.test;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserTestDto {

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "비밀번호")
    private String password;

    @Schema(description = "생년월일",example = "1999-01-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birth;

    @Schema(description = "핸드폰번호")
    private String phone;

    @Schema(description = "성별", example = "M")
    @Pattern(regexp = "^[MF]$")
    private String gender;

    @Schema(description = "권한", defaultValue = "1")
    private String role;

    @Schema(description = "활성상태", defaultValue = "1")
    private int status;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "이미지url")
    private String imgUrl;

    @Schema(description = "소셜 로그인 제공자")
    private String provider;

    @Schema(description = "유저 식별자")
    private String providerId;

}
