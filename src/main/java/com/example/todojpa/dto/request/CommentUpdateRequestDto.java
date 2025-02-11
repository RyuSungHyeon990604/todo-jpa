package com.example.todojpa.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentUpdateRequestDto {
    @NotNull @Size(max = 50)
    private String comment;
}
