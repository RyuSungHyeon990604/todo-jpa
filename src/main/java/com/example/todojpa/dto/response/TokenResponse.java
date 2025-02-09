package com.example.todojpa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    String accessToken;
    String refreshToken;
}
