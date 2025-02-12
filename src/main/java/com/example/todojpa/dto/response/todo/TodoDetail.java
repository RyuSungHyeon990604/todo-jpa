package com.example.todojpa.dto.response.todo;

import com.example.todojpa.dto.response.comment.CommentDetail;
import com.example.todojpa.entity.Todo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class TodoDetail {
    private final Long id;
    private final String userName;
    private final String title;
    private final String task;
    private final Long commentCount;
    private final LocalDateTime createdAt;
    private final List<CommentDetail> comments;

    public TodoDetail(Todo todo, Long commentCount) {
        this.id = todo.getId();
        this.userName = todo.getUser().getName();
        this.title = todo.getTitle();
        this.task = todo.getTask();
        this.commentCount = commentCount;
        this.createdAt = todo.getCreatedAt();
        this.comments = new ArrayList<>();
    }

    public static TodoDetail from(Todo todo) {
        return new TodoDetail(todo.getId(), todo.getUser().getName(), todo.getTitle(), todo.getTask(), (long) todo.getCommentCount(), todo.getUpdatedAt(), CommentDetail.convertTree(todo.getComments()));
    }
}
