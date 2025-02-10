package com.example.todojpa.dto.response;

import com.example.todojpa.entity.Todo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoDetail {
    private Long id;
    private String userName;
    private String title;
    private String task;
    private int commentSize;
    private Set<CommentResponse> comments;

    public static TodoDetail from(Todo todo) {
        return new TodoDetail(todo.getId(), todo.getUser().getName(), todo.getTitle(), todo.getTask(), todo.getComments().size(), CommentResponse.convertTree(todo.getComments()));
    }
}
