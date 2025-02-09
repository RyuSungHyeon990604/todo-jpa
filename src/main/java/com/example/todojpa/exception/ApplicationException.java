package com.example.todojpa.exception;

public class ApplicationException extends RuntimeException {
    ErrorCode error;
    public ApplicationException(ErrorCode error) {
        super(error.getMessage());
        this.error = error;
    }
    public String getErrorCode(){
        return error.getCode();
    }
}
