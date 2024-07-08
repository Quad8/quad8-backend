package site.keydeuk.store.common.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import site.keydeuk.store.common.response.CommonResponse;
import site.keydeuk.store.common.security.authentication.dto.AuthenticationToken;
import site.keydeuk.store.common.security.authentication.token.TokenService;

import java.io.IOException;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        String userId = authentication.getName();
        log.info("Authentication Name = {}", userId);

        String randomToken = UUID.randomUUID().toString();
        AuthenticationToken authenticationToken = tokenService.generatedToken(randomToken, userId);

        addTokenToHeader(response, authenticationToken);
        sendResponse(response, authenticationToken);
    }

    private void addTokenToHeader(HttpServletResponse response, AuthenticationToken authenticationToken) {
        String accessToken = "accessToken=" + authenticationToken.accessToken() + "; HttpOnly; Secure; Path=/; Max-Age=3600";
        String refreshToken = "refreshToken=" + authenticationToken.refreshToken() + "; HttpOnly; Secure; Path=/; Max-Age=604800";

        response.setHeader("Set-Cookie", accessToken);
        response.addHeader("Set-Cookie", refreshToken);
    }

    private void sendResponse(HttpServletResponse response, AuthenticationToken authenticationToken) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        objectMapper.writeValue(response.getWriter(), CommonResponse.ok(authenticationToken));
    }
}