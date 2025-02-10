package com.example.todojpa.dto.response;

import com.example.todojpa.entity.Todo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoDetail {
    private final Long id;
    private final String userName;
    private final String title;
    private final String task;
    private final int commentSize;
    private final LocalDateTime createdAt;
    private final Set<CommentResponse> comments;

    public static TodoDetail from(Todo todo) {
        return new TodoDetail(todo.getId(), todo.getUser().getName(), todo.getTitle(), todo.getTask(), todo.getComments().size(), todo.getUpdatedAt(), CommentResponse.convertTree(todo.getComments()));
    }
}
