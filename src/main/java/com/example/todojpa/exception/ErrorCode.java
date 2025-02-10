package com.example.todojpa.exception;

public enum ErrorCode {
    INVALID_TOKEN("0002", "Invalid token"),
    BAD_REQUEST("0001", "Bad Request"),
    EXPIRED_TOKEN("0004", "Expired token"),
    EMAIL_ALREADY_EXISTS("0005", "Email already exists"),
    WRONG_PASSWORD("0007", "Wrong password"),
    USER_NOT_FOUND("0005", "User not found"),
    ACCESS_DENIED("0006", "Access denied"),
    METHOD_ARGUMENT_NOT_VALID("0002","입력값이 올바르지 않습니다"),
    MISSING_REQUEST_HEADER("0003","필수요청 헤더가 누락되었습니다"),
    TODO_NOT_FOUND("0006", "Todo not found"),
    COMMENT_NOT_FOUND("0007", "Comment not found");
    private final String code;
    private final String message;


    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
