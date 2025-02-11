package com.example.todojpa.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateRequestDto {
    @NotBlank
    String name;
    @NotBlank @Size(min = 8, max = 50)
    String password;
    @NotBlank @Email
    String email;
}

