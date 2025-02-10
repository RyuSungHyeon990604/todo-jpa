package com.example.todojpa.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAuthentication {
    private Long userId;
    private Boolean isValid = false;
}
