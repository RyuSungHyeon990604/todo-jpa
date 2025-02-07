package com.example.todojpa.dto;

import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    private String name;
    private String email;
    private String password;
}
