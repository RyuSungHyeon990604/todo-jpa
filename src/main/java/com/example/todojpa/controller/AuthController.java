package com.example.todojpa.controller;

import com.example.todojpa.dto.request.LoginRequestDto;
import com.example.todojpa.dto.response.TokenResponse;
import com.example.todojpa.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginJwt(@RequestBody LoginRequestDto loginRequestDto) {
        TokenResponse token = authService.loginJwt(loginRequestDto);

        return ResponseEntity.ok(token);
    }

    @GetMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue() {
        TokenResponse tokenResponse = authService.reissueAccessToken();

        return ResponseEntity.ok(tokenResponse);
    }
}
