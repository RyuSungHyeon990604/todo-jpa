package com.example.todojpa.exception.handler;

import com.example.todojpa.exception.ApplicationException;
import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ExceptionResponse> handleApplicationException(ApplicationException e) {
        log.warn(e.getMessage(),e);
        ExceptionResponse response = new ExceptionResponse(e.getMessage(),e.getErrorCode());

        return ResponseEntity.badRequest().body(response);
    }

    //@Valid request 유효성 체크
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage(),e);
        String code = ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode();
        String errorMessage = ErrorCode.METHOD_ARGUMENT_NOT_VALID.getMessage();
        // 유효성 검사 실패한 모든 필드와 에러 메시지를 추출
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, String> errorFields = fieldErrors.stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,(m1,m2)->m1));

        ExceptionResponse response = new ExceptionResponse(errorMessage, code, errorFields);
        return ResponseEntity.badRequest().body(response);
    }

    //@Validated 예외처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn(e.getMessage(), e);
        String code = ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode();
        String errorMessage = ErrorCode.METHOD_ARGUMENT_NOT_VALID.getMessage();

        // 필드 이름과 메시지 추출
        Map<String, String> errorFields = e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),  // 필드 이름
                        violation -> violation.getMessage()));  // 오류 메시지

        ExceptionResponse response = new ExceptionResponse(errorMessage, code, errorFields);
        return ResponseEntity.badRequest().body(response);
    }
}
