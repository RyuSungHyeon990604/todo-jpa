package com.example.todojpa.annotation;

import com.example.todojpa.exception.ApplicationException;
import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.security.MySecurityContextHolder;
import com.example.todojpa.security.UserAuthentication;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isType = parameter.getParameterType().equals(LoginUserDto.class);
        boolean hasAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        return isType && hasAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return Optional.ofNullable(MySecurityContextHolder.getAuthenticated())
                .filter(UserAuthentication::getIsValid)
                .map(auth -> new LoginUserDto(auth.getUserId()))
                .orElseThrow(() -> new ApplicationException(ErrorCode.ACCESS_DENIED));
    }
}
