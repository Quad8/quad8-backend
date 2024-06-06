package site.keydeuk.store.domain.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;
import site.keydeuk.store.domain.security.JwtService;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.domain.security.constants.JwtConstants;
import site.keydeuk.store.domain.security.dto.RefreshToken;
import site.keydeuk.store.domain.security.utils.JwtUtils;
import site.keydeuk.store.entity.User;
import site.keydeuk.store.entity.enums.RoleType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * OAuth2 로그인 성공 후 특정 url로 리다이렉트
 */
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();
        String accessToken = JwtUtils.generateAccessToken(user);
        // 최초 로그인인 경우 추가 정보 입력을 위한 회원가입 페이지로 리다이렉트
        if (user.getRole().equals(RoleType.ROLE_GUEST)) {
            log.info("소셜 로그인 성공 -> 가입 안되어 있는 유저인것 확인 -> 회원가입 페이지 이동");
            log.info("{}", user);
            response.addHeader(JwtConstants.ACCESS, JwtConstants.JWT_TYPE + accessToken);
            String redirectURL = UriComponentsBuilder.fromUriString("http://localhost:8080/oauth2/signUp")
                    .queryParam("email", user.getEmail())
                    .queryParam("provider", user.getProvider())
                    .queryParam("providerId", user.getProviderId())
                    .queryParam("nickname",user.getNickname())
                    .queryParam("imgUrl",user.getImgUrl())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, redirectURL);
        } else {
            String refreshToken = JwtUtils.generateRefreshToken(user);
            jwtService.save(new RefreshToken(refreshToken, user.getId()));

            response.addHeader(JwtConstants.ACCESS, JwtConstants.JWT_TYPE + accessToken);
            response.addHeader(JwtConstants.REFRESH, JwtConstants.JWT_TYPE + refreshToken);

            // 최초 로그인이 아닌 경우 로그인 성공 페이지로 이동
            log.info("소셜 로그인 성공 -> 로그인 완료 페이지 이동");
            String redirectURL = UriComponentsBuilder.fromUriString("http://localhost:8080/loginSuccess")
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, redirectURL);
        }
    }
}
