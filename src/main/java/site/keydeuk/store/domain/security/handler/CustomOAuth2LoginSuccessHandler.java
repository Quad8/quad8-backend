package site.keydeuk.store.domain.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.common.security.authentication.dto.AuthenticationToken;
import site.keydeuk.store.common.security.authentication.token.TokenService;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.entity.User;
import site.keydeuk.store.entity.enums.RoleType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * OAuth2 로그인 성공 후 특정 url로 리다이렉트
 */
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            log.info("OAuth2 login success");
            User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();
            log.info("Principal Details: {}", ((PrincipalDetails) authentication.getPrincipal()).getUser());

            // 최초 로그인인 경우 추가 정보 입력을 위한 회원가입 페이지로 리다이렉트
            if (user.getRole().equals(RoleType.ROLE_GUEST)) {
                log.info("소셜 로그인 성공 -> 가입 안되어 있는 유저인 것 확인 -> 회원가입 페이지 이동");

                String redirectURL = UriComponentsBuilder.fromUriString("https://keydeuk-be.shop/api/v1/oauth2/signUp")
                        .queryParam("email", user.getEmail())
                        .queryParam("provider", user.getProvider())
                        .queryParam("providerId", user.getProviderId())
                        .queryParam("nickname", user.getNickname())
                        .queryParam("imgUrl", user.getImgUrl())
                        .build()
                        .encode(StandardCharsets.UTF_8)
                        .toUriString();

                log.debug("회원가입으로 redirect: " + redirectURL);
                clearAuthenticationAttributes(request);
                response.sendRedirect(redirectURL);
            } else {
                // 로그인 성공 시의 기본 처리
                log.debug("기존 회원으로 로그인 성공");
                response.sendRedirect("/dashboard");
            }
        } catch (Exception e) {
            log.error("OAuth2 로그인 성공 핸들러에서 오류 발생: ", e);
            throw e;
        }
    }

    private void sendResponse(HttpServletResponse response, AuthenticationToken authenticationToken) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        objectMapper.writeValue(response.getWriter(), CommonResponse.ok(authenticationToken));
    }
}
