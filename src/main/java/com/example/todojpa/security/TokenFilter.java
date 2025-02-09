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

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
public class TokenFilter implements Filter {

    // 예외 처리할 경로 + 메서드 조합 (예: GET /api 허용, POST /api 검증)
    private final Set<String> excludedPaths = Set.of("GET:/todos", "POST:/users", "GET:/users" ,"POST:/auth/login");
    private final JwtProvider jwtProvider;

    public TokenFilter(JwtProvider jwtProvider) {
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
        if(excludedPaths.contains(method+":"+url)) {
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

        String accessToken = authorization.split(" ")[1];

        try {
            jwtProvider.validateToken(accessToken);
        } catch (ExpiredJwtException e) {
            log.warn("Expired Access token");
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.EXPIRED_TOKEN);
            return;
        } catch (JwtException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.INVALID_TOKEN);
            return;
        }

        filterChain.doFilter(request, response);
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
