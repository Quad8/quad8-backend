package site.keydeuk.store.domain.oauth2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Tag(name = "OAuth2", description = "OAuth2 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/oauth2")
public class OAuth2Controller {
    @Operation(summary = "OAuth2 회원가입", description = "OAuth2를 통해 처음 로그인을 했다면 회원가입 페이지로 리다이렉트합니다. 아래 파라미터가 리다이렉트 되는 주소 뒤에 붙어져서 리다이렉트 됩니다")
    @GetMapping("/signUp")
    public RedirectView loadOAuthSignUp(
            @Parameter(description = "사용자 이메일", example = "user@example.com") @RequestParam String email,
            @Parameter(description = "OAuth2 제공자", example = "GOOGLE") @RequestParam String provider,
            @Parameter(description = "OAuth2 제공자 ID", example = "1234567890") @RequestParam String providerId,
            @Parameter(description = "사용자 닉네임", example = "nickname123") @RequestParam String nickname,
            @Parameter(description = "사용자 프로필 이미지 URL", example = "http://example.com/image.jpg") @RequestParam String imgUrl) throws UnsupportedEncodingException, UnsupportedEncodingException {

        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
        String encodedProvider = URLEncoder.encode(provider, StandardCharsets.UTF_8);
        String encodedProviderId = URLEncoder.encode(providerId, StandardCharsets.UTF_8);
        String encodedNickname = URLEncoder.encode(nickname, StandardCharsets.UTF_8);
        String encodedImgUrl = URLEncoder.encode(imgUrl, StandardCharsets.UTF_8);

        String redirectUrl = String.format("https://keydeuk.com/sign-up?email=%s&provider=%s&providerId=%s&nickname=%s&imgUrl=%s",
                encodedEmail, encodedProvider, encodedProviderId, encodedNickname, encodedImgUrl);

        return new RedirectView(redirectUrl);
    }
}
