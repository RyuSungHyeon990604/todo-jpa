package com.example.todojpa.dto.request.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoCreateRequestDto {
    @NotBlank @Size(min = 2, max = 50)
    private String title;

    @Size(max = 250)
    private String task;
}
