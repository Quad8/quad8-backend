package site.keydeuk.store.common.security.authentication.dto;

import org.springframework.util.StringUtils;
import site.keydeuk.store.common.exception.CustomException;

import static site.keydeuk.store.common.response.ErrorCode.EMPTY_ACCESS_TOKEN;
import static site.keydeuk.store.common.response.ErrorCode.EMPTY_REFRESH_TOKEN;

public record LogoutRequest(
        String refreshToken,
        String accessToken
) {

    public LogoutRequest {
        if (!StringUtils.hasText(refreshToken)) {
            throw new CustomException(EMPTY_REFRESH_TOKEN);
        }

        if (!StringUtils.hasText(accessToken)) {
            throw new CustomException(EMPTY_ACCESS_TOKEN);
        }
    }
}
