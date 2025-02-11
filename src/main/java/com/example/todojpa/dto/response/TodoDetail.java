package com.example.todojpa.dto.response;

import com.example.todojpa.entity.BaseEntity;
import com.example.todojpa.entity.DeleteCheckEntity;
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
    private final Long commentCount;
    private final LocalDateTime createdAt;

    public static TodoDetail from(Todo todo) {
        return new TodoDetail(todo.getId(), todo.getUser().getName(), todo.getTitle(), todo.getTask(), todo.getCommentCount(), todo.getUpdatedAt());
    }
}
