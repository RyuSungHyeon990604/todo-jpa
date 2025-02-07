package com.example.todojpa.dto;

import com.example.todojpa.entity.Todo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoDetail {
    private Long id;
    private String userName;
    private String title;
    private String task;
    private List<CommentResponse> comments = new ArrayList<>();

    public static TodoDetail from(Todo todo) {
        return new TodoDetail(todo.getId(), todo.getUser().getName(), todo.getTitle(), todo.getTask(), todo.getComments().stream().map(CommentResponse::from).toList());
    }
}
