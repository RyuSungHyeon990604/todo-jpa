package com.example.todojpa.dto.request;

import lombok.Getter;

@Getter
public class CommentCreateRequestDto {
    private String comment;
    private Long parent;
}

