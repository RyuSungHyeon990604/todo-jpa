package com.example.todojpa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    private final String  accessToken;
    private final String refreshToken;
}
