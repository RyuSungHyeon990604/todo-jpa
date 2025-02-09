package com.example.todojpa.security;

import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.exception.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
@Component
public class LoginFilter implements Filter {

    // 예외 처리할 경로 (메서드:url)
    private final String[] whiteList = {
            "GET:/", //그냥 해봄
            "GET:/todos", //일정조회
            "GET:/todos/*", //일정 단건 조회
            "POST:/auth/login", //로그인
            "GET:/users", //사용자 조회
            "POST:/users", //사용자 생성, 회원가입
            "GET:/users/*" //사용자 단건 조회
    };
    private final JwtProvider jwtProvider;

    public LoginFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authorization = request.getHeader("Authorization");

        String method = request.getMethod();
        String url = request.getRequestURI();

        //특정 요청 예외처리
        if(isWhiteList(method +":"+url)) {
            filterChain.doFilter(request, response);
            return;
        }

        //필수 헤더 누락 메시지 보내기
        if (authorization == null) {
            log.warn("Authorization are missing");
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.MISSING_REQUEST_HEADER);
            return;
        }

        //Bearer 접두사 없으면 잘못된 요청
        if(!authorization.split(" ")[0].equals("Bearer") || authorization.split(" ").length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String token = authorization.split(" ")[1];

        try {
            jwtProvider.validateToken(token);
        } catch (ExpiredJwtException e) {
            log.warn("Expired Access token",e);
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.EXPIRED_TOKEN);
            return;
        } catch (JwtException e) {
            log.warn("JWT token validation failed",e);
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.INVALID_TOKEN);
            return;
        }
        //토큰 검증을 통과했다면 ContextHolder에 사용자정보 등록
        MySecurityContextHolder.setAuthenticated(new UserAuthentication(jwtProvider.getUserId(token), true));
        try {
            filterChain.doFilter(request, response);
        } finally {
            log.info("MySecurityContextHolder.clear()");
            MySecurityContextHolder.clear();
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 객체 생성
        ExceptionResponse errorResponse = new ExceptionResponse(errorCode.getCode(), errorCode.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    public Boolean isWhiteList(String requestUrl){
        return PatternMatchUtils.simpleMatch(whiteList, requestUrl);
    }
}
