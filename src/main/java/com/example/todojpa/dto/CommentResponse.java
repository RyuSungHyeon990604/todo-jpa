package com.example.todojpa.dto;

import com.example.todojpa.entity.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {
    private String comment;
    private Long todoId;
    private Long from;

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getComment(), comment.getTodo().getId(), comment.getUser().getId());
    }
}
