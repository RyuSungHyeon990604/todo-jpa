package com.example.todojpa.annotation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface DateCheck {
    String message() default "yyyy-MM-dd 형식으로 입력해주세요";
    Class[] groups() default {};
    Class[] payload() default {};
}
