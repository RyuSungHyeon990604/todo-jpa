package com.example.todojpa.controller;

import com.example.todojpa.dto.request.login.LoginRequestDto;
import com.example.todojpa.dto.response.token.TokenResponse;
import com.example.todojpa.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginJwt(@RequestBody LoginRequestDto loginRequestDto,
                                                  HttpServletResponse response) {
        TokenResponse token = authService.loginJwt(loginRequestDto);
        response.setHeader("Authorization", "Bearer " + token.getAccessToken());

        //refresh 토큰은 httpOnly에 담는다
        setCookie(response, token.getRefreshToken());



        return ResponseEntity.ok(token);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(HttpServletResponse response) throws UnsupportedEncodingException {
        TokenResponse tokenResponse = authService.reissueAccessToken();
        response.setHeader("Authorization", "Bearer "+tokenResponse.getAccessToken());

        //refresh 토큰은 httpOnly에 담는다
        setCookie(response, tokenResponse.getRefreshToken());

        return ResponseEntity.ok(tokenResponse);
    }

    public void setCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh", refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
