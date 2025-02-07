package com.example.todojpa.dto;

import com.example.todojpa.entity.Todo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoResponse {
    private Long id;
    private String userName;
    private String title;
    private String task;

    public static TodoResponse from(Todo todo) {
        return new TodoResponse(todo.getId(),todo.getUser().getName(),todo.getTitle(),todo.getTask());
    }
}
