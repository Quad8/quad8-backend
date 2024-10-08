package site.keydeuk.store.common.security.authentication.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.keydeuk.store.common.exception.CustomException;
import site.keydeuk.store.common.redis.service.RedisService;
import site.keydeuk.store.common.security.authentication.dto.AuthenticationToken;
import site.keydeuk.store.common.security.authentication.token.dto.RefreshToken;
import site.keydeuk.store.common.security.authentication.token.dto.Token;
import site.keydeuk.store.common.security.exception.InvalidJwtException;
import site.keydeuk.store.common.security.exception.SaveTokenException;

import java.time.Instant;

import static site.keydeuk.store.common.response.ErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    public static final String REDIS_REFERS_TOKEN_PREFIX = "RefreshToken:";
    public static final String REDIS_BLACK_LIST_PREFIX = "BlackList:";
    public static final String BLACK_LIST_DEFAULT_VALUE = "Logout";
    public static final int VALID_MINIMUM_TIME = 0;

    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    @Override
    public String reissueToken(String refreshToken, String accessToken) {
        try {
            String randomToken = tokenProvider.getSubject(refreshToken);
            log.info("Fetch randomToken = [{}]", randomToken);

            RefreshToken token = redisService.get(REDIS_REFERS_TOKEN_PREFIX + randomToken, RefreshToken.class)
                    .orElseThrow(() -> new CustomException(INVALID_TOKEN));
            log.info("Fetch userId = [{}]", token.email());

            if (!refreshToken.equals(token.refreshToken())) {
                throw new CustomException(INVALID_TOKEN);
            }

            addBlackList(accessToken);
            AuthenticationToken authenticationToken = generateNewAccessToken(token.email(), refreshToken);
            return authenticationToken.accessToken();
        } catch (ExpiredJwtException e) {
            log.info("[{}] 이미 만료된 토큰입니다.", refreshToken);
            throw new CustomException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new CustomException(INVALID_TOKEN);
        }
    }

    @Override
    public AuthenticationToken generatedToken(String randomToken, String userId) {
        Token token = tokenProvider.generateToken(randomToken, userId);
        try {
            RefreshToken refreshToken = createRefreshToken(userId, token);
            redisService.set(REDIS_REFERS_TOKEN_PREFIX + randomToken, refreshToken, token.refreshTokenExpired());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SaveTokenException(SAVE_REFRESH_TOKEN_FAILED);
        }

        return AuthenticationToken.of(token);
    }

    @Override
    public String getUsername(String token)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return tokenProvider.getUsername(token);
    }

    @Override
    public void removeRefreshToken(String refreshToken) {
        try {
            String randomToken = tokenProvider.getSubject(refreshToken);
            log.info("Remove randomToken = [{}]", randomToken);

            if (redisService.delete(REDIS_REFERS_TOKEN_PREFIX + randomToken)) {
                log.info("[{}] RefreshToken 삭제 성공", randomToken);
            }
        } catch (ExpiredJwtException e) {
            log.info("[{}] 이미 만료된 토큰입니다.", refreshToken);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new InvalidJwtException(INVALID_TOKEN, e);
        }
    }

    @Override
    public void addBlackList(String accessToken) {
        try {
            long expiredIn = tokenProvider.getExpiration(accessToken);
            long validTime = expiredIn - Instant.now().getEpochSecond();

            log.info("[{}] AccessToken의 남은 만료 시간은 {}초 입니다.", tokenProvider.getUsername(accessToken), validTime);
            if (validTime > VALID_MINIMUM_TIME) {
                redisService.set(REDIS_BLACK_LIST_PREFIX + accessToken, BLACK_LIST_DEFAULT_VALUE, validTime);
            }
        } catch (ExpiredJwtException e) {
            log.info("[{}] 이미 만료된 토큰입니다.", accessToken);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new InvalidJwtException(INVALID_TOKEN, e);
        }
    }

    @Override
    public boolean isBlackListToken(String token) {
        return redisService.get(REDIS_BLACK_LIST_PREFIX + token, String.class).isPresent();
    }

    private RefreshToken createRefreshToken(String email, Token token) {
        return RefreshToken.builder()
                .email(email)
                .refreshToken(token.refreshToken())
                .build();
    }

    private AuthenticationToken generateNewAccessToken(String username, String existingRefreshToken) {
        Token token = tokenProvider.generateTokenWithExistingRefreshToken(username, existingRefreshToken);
        return AuthenticationToken.of(token);
    }
}
