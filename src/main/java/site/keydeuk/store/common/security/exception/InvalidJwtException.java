package site.keydeuk.store.common.security.exception;

import org.springframework.security.core.AuthenticationException;
import site.keydeuk.store.common.response.ErrorCode;

public class InvalidJwtException extends AuthenticationException {

    public InvalidJwtException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public InvalidJwtException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
    }
}
