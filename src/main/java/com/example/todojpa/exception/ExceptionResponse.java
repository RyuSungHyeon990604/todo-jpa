package com.example.todojpa.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ExceptionResponse {
    String message;
    String code;
    private Map<String, String> fieldErrors;
    public ExceptionResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public ExceptionResponse(String message, String code, Map<String, String> fieldErrors) {
        this.message = message;
        this.code = code;
        this.fieldErrors = fieldErrors;
    }
}
