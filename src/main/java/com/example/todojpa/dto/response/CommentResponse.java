package com.example.todojpa.dto.response;

import com.example.todojpa.entity.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {
    Set<CommentDetail> data;
    PageInfo pageInfo;

    public static CommentResponse from(List<Comment> comments) {
        return new CommentResponse(CommentDetail.convertTree(comments), null);
    }
}
