package com.example.todojpa.security;

import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.exception.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ReissueFilter implements Filter {

    private final JwtProvider jwtProvider;

    public ReissueFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }
        if (refreshToken == null) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.ACCESS_DENIED);
            return;
        }


        try {
            Claims claims = jwtProvider.validateToken(refreshToken);
            String tokenType = claims.get("type",String.class);
            //엑세스토큰이 아니라면 오류 반환
            if(!tokenType.equals("refresh")) {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.INVALID_TOKEN);
                return;
            }
        } catch (ExpiredJwtException e) {
            log.warn("Expired Refresh token",e);
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.EXPIRED_TOKEN);
            return;
        } catch (JwtException e) {
            log.warn("JWT token validation failed",e);
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.INVALID_TOKEN);
            return;
        }
        //토큰 검증을 통과했다면 ContextHolder에 사용자정보 등록
        MySecurityContextHolder.setAuthenticated(new UserAuthentication(jwtProvider.getUserId(refreshToken), true));
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
}
