package site.keydeuk.store.common.security.authentication.token.service;

import site.keydeuk.store.common.security.authentication.dto.AuthenticationToken;

public interface TokenService {

    AuthenticationToken reissueToken(String refreshToken);

    AuthenticationToken generatedToken(String randomToken, String email);

    String getUsername(String token);

    void removeRefreshToken(String refreshToken);

    void addBlackList(String accessToken);

    boolean isBlackListToken(String token);
}
