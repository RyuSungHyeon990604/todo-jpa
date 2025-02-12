package com.example.todojpa.security;

import com.example.todojpa.entity.User;
import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.exception.ExceptionResponse;
import com.example.todojpa.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.sql.Timestamp;

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
            "GET:/users/*", //사용자 단건 조회
            "GET:/comments*",//댓글조회
            "POST:/auth/reissue"
    };
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public LoginFilter(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
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

        //헤더에서 토큰 분리
        String token = authorization.split(" ")[1];

        try {
            //토큰 검증
            Claims claims = jwtProvider.validateToken(token);

            //엑세스토큰이 아니라면 오류 반환
            String tokenType = claims.get("type",String.class);
            if(!tokenType.equals("access")) {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.INVALID_TOKEN);
                return;
            }

        } catch (ExpiredJwtException e) {
            log.warn("Expired Access token",e);
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.EXPIRED_TOKEN);
            return;
        } catch (JwtException e) {
            log.warn("JWT token validation failed",e);
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.INVALID_TOKEN);
            return;
        }

        //사용자 로그아웃이후에 발급한 토큰인지 검증, 이전에 발급한거라면 사용불가한 토큰
        Long userId = jwtProvider.getUserId(token);
        Timestamp lastLogoutTime = userRepository.findLastLogoutTimeById(userId);
        if(lastLogoutTime != null && jwtProvider.isIssuedBefore(token, lastLogoutTime)) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorCode.INVALID_TOKEN);
            return;
        }

        //모든 검증을 통과했다면 ContextHolder에 사용자정보 등록
        MySecurityContextHolder.setAuthenticated(new UserAuthentication(userId, true));
        try {
            filterChain.doFilter(request, response);
        } finally {
            log.info("MySecurityContextHolder.clear()");
            //요청 로직이후 ContextHolder 비우기
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
