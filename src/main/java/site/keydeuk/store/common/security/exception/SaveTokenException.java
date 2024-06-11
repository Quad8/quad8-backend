package site.keydeuk.store.common.security.exception;

import org.springframework.security.core.AuthenticationException;
import site.keydeuk.store.common.response.ErrorCode;

public class SaveTokenException extends AuthenticationException {

    public SaveTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
