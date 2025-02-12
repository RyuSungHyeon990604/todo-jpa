package com.example.todojpa.dto.response.todo;

import com.example.todojpa.dto.response.comment.CommentDetail;
import com.example.todojpa.entity.Todo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoDetail {
    private final Long id;
    private final String userName;
    private final String title;
    private final String task;
    private final int commentCount;
    private final LocalDateTime createdAt;
    private final List<CommentDetail> comments;

    public static TodoDetail from(Todo todo) {
        return new TodoDetail(todo.getId(), todo.getUser().getName(), todo.getTitle(), todo.getTask(), todo.getCommentCount(), todo.getUpdatedAt(), CommentDetail.convertTree(todo.getComments()));
    }
}
