package site.keydeuk.store.common.security.authentication.token;

import site.keydeuk.store.common.security.authentication.dto.AuthenticationToken;

public interface TokenService {

    AuthenticationToken reissueToken(String refreshToken, String accessToken);

    AuthenticationToken generatedToken(String randomToken, String email);

    String getUsername(String token);

    void removeRefreshToken(String refreshToken);

    void addBlackList(String accessToken);

    boolean isBlackListToken(String token);
}
