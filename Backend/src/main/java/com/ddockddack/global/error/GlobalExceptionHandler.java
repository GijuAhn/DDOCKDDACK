package com.ddockddack.global.error;

import com.ddockddack.global.error.exception.AccessDeniedException;
import com.ddockddack.global.error.exception.NotFoundException;
import com.ddockddack.global.error.exception.NumberOfFileExceedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_AUTHORIZED);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.NOT_AUTHORIZED.getCode()));
    }

    /**
     * 베스트 컷 이미지 10개 초과시 발생하는 예외 처리
     */
    @ExceptionHandler(NumberOfFileExceedException.class)
    protected ResponseEntity<ErrorResponse> handleNumberOfFileExceedException(
            NumberOfFileExceedException e) {
        log.error("handleNumberOfFileExceedException", e);
        return new ResponseEntity<>(ErrorResponse.of(e.getErrorCode()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 존재하지 않는 리소스에 대한 예외 처리
     */
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        log.error("handleNotFoundException", e);
        return new ResponseEntity<>(ErrorResponse.of(e.getErrorCode()), HttpStatus.NOT_FOUND);
    }
}