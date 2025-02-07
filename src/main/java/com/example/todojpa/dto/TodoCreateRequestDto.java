package com.example.todojpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoCreateRequestDto {
    private String title;
    private String task;
}
