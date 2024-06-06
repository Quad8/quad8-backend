package site.keydeuk.store.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import site.keydeuk.store.common.response.CommonResponse;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public CommonResponse<Void> handleNoSuchElementException(NoSuchElementException ex) {
        return CommonResponse.error(ex.getMessage());
    }
}
