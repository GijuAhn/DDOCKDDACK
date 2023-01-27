package com.ddockddack.global.error;

public class AccessDeniedException extends RuntimeException {
    private ErrorCode errorCode;

    public AccessDeniedException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
