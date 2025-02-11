package com.example.todojpa.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import javax.annotation.processing.Generated;

@Getter
public class UserDeleteRequestDto {
    @NotBlank
    String password;
}
