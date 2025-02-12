package com.example.todojpa.exception;

public enum ErrorCode {
    INVALID_TOKEN("0001", "Invalid token"),
    BAD_REQUEST("0002", "Bad Request"),
    EXPIRED_TOKEN("0003", "Expired token"),
    EMAIL_ALREADY_EXISTS("0004", "Email already exists"),
    WRONG_PASSWORD("0005", "Wrong password"),
    USER_NOT_FOUND("0006", "User not found"),
    ACCESS_DENIED("0007", "Access denied"),
    METHOD_ARGUMENT_NOT_VALID("0008","입력값이 올바르지 않습니다"),
    MISSING_REQUEST_HEADER("0009","필수요청 헤더가 누락되었습니다"),
    TODO_NOT_FOUND("0010", "Todo not found"),
    COMMENT_NOT_FOUND("0011", "Comment not found"),
    USER_ACCOUNT_DELETED("0012", "해당 계정은 탈퇴 처리된 회원입니다.");
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
