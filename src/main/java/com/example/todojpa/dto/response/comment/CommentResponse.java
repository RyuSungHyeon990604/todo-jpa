package com.example.todojpa.dto.response.comment;

import com.example.todojpa.dto.response.PageInfo;
import com.example.todojpa.entity.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {
    List<CommentDetail> data;
    PageInfo pageInfo;

    public static CommentResponse from(List<CommentDetail> comments) {
        return new CommentResponse(comments, null);
    }

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(List.of(CommentDetail.from(comment)),null);
    }
}
