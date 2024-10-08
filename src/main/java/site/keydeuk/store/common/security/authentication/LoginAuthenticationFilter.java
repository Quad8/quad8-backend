package site.keydeuk.store.common.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import site.keydeuk.store.common.security.authentication.dto.LoginRequest;
import site.keydeuk.store.common.security.exception.InvalidAuthenticationArgumentException;

import java.io.IOException;

import static site.keydeuk.store.common.response.ErrorCode.COMMON_INVALID_PARAMETER;


@Slf4j
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/login", "POST");

    private final ObjectMapper objectMapper;

    public LoginAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        LoginRequest loginRequest = getLoginInfo(request);
        String email = loginRequest.email();
        String password = loginRequest.password();
        log.info("Login email = {} / password = {}", email, password);

        UsernamePasswordAuthenticationToken authRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(email, password);
        return getAuthenticationManager().authenticate(authRequest);
    }

    private LoginRequest getLoginInfo(HttpServletRequest request) {
        try {
            return objectMapper.readValue(request.getReader(), LoginRequest.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InvalidAuthenticationArgumentException(COMMON_INVALID_PARAMETER);
        }
    }
}
