package site.keydeuk.store.domain.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.common.security.authentication.dto.AuthenticationToken;
import site.keydeuk.store.common.security.authentication.token.TokenService;
import site.keydeuk.store.domain.security.PrincipalDetails;
import site.keydeuk.store.domain.security.constants.JwtConstants;
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
        log.info("oauth2 login success");
        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();
        log.info("{}", ((PrincipalDetails) authentication.getPrincipal()).getUser());

        // 최초 로그인인 경우 추가 정보 입력을 위한 회원가입 페이지로 리다이렉트
        if (user.getRole().equals(RoleType.ROLE_GUEST)) {
            log.info("소셜 로그인 성공 -> 가입 안되어 있는 유저인것 확인 -> 회원가입 페이지 이동");
            log.info("{}", user);

            String randomToken = UUID.randomUUID().toString();
            AuthenticationToken authenticationToken = tokenService.generatedToken(randomToken, user.getEmail());//여기 작성해야함

            response.addHeader(JwtConstants.ACCESS, JwtConstants.JWT_TYPE + authenticationToken); //헤더 추가 안됨

            log.info("{}", authenticationToken);
            log.info("{}", response.getHeader(JwtConstants.ACCESS));

            String redirectURL = UriComponentsBuilder.fromUriString("http://localhost:8080/oauth2/signUp")
                    .queryParam("email", user.getEmail())
                    .queryParam("provider", user.getProvider())
                    .queryParam("providerId", user.getProviderId())
                    .queryParam("nickname", user.getNickname())
                    .queryParam("imgUrl", user.getImgUrl())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            // 응답 본문에 토큰을 포함시킴
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            objectMapper.writeValue(response.getWriter(), CommonResponse.ok(authenticationToken));

            getRedirectStrategy().sendRedirect(request, response, redirectURL);
        } else {
            String email = authentication.getName();
            log.info("Authentication Name = {}", email);

            String newRandomToken = UUID.randomUUID().toString();
            AuthenticationToken authenticationToken = tokenService.generatedToken(newRandomToken, email);

            sendResponse(response, authenticationToken);
        }
    }

    private void sendResponse(HttpServletResponse response, AuthenticationToken authenticationToken) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        objectMapper.writeValue(response.getWriter(), CommonResponse.ok(authenticationToken));
    }
}
