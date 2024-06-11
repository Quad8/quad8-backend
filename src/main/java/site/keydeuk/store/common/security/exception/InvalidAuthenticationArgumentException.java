package site.keydeuk.store.common.security.exception;

import org.springframework.security.core.AuthenticationException;
import site.keydeuk.store.common.response.ErrorCode;

public class InvalidAuthenticationArgumentException extends AuthenticationException {

    public InvalidAuthenticationArgumentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
