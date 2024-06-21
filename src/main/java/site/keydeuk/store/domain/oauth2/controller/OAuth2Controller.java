package site.keydeuk.store.domain.oauth2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Tag(name = "OAuth2", description = "OAuth2 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/oauth2")
public class OAuth2Controller {

    @Operation(summary = "OAuth2 회원가입", description = "OAuth2를 통해 처음 로그인을 했다면 회원가입 페이지로 리다이렉트합니다. 아래 파라미터가 리다이렉트 되는 주소 뒤에 붙어져서 리다이렉트 됩니다")
    @GetMapping("/signUp")
    public String loadOAuthSignUp(
            @Parameter(description = "사용자 이메일", example = "user@example.com") @RequestParam String email,
            @Parameter(description = "OAuth2 제공자", example = "GOOGLE") @RequestParam String provider,
            @Parameter(description = "OAuth2 제공자 ID", example = "1234567890") @RequestParam String providerId,
            @Parameter(description = "사용자 닉네임", example = "nickname123") @RequestParam String nickname,
            @Parameter(description = "사용자 프로필 이미지 URL", example = "http://example.com/image.jpg") @RequestParam String imgUrl,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("email", email);
        redirectAttributes.addAttribute("provider", provider);
        redirectAttributes.addAttribute("providerId", providerId);
        redirectAttributes.addAttribute("nickname", nickname);
        redirectAttributes.addAttribute("imgUrl", imgUrl);
        return "redirect:http://localhost:3000/sign-up"; // 프론트 회원가입 주소로 리다이렉트
    }

}
