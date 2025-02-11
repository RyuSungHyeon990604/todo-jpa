package com.example.todojpa.dto.request.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentCreateRequestDto {
    @NotNull @Size(max = 250)
    private String comment;

    private Long parent;
}

