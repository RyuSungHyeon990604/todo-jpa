package com.example.todojpa.dto.response;

import com.example.todojpa.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class TodoResponse {
    private List<TodoDetail> data;
    private PageInfo pageInfo;

    public static TodoResponse from(Page<Todo> todos) {
        List<TodoDetail> todoList = todos.getContent().stream().map(TodoDetail::from).toList();
        return new TodoResponse(todoList, new PageInfo(todos));
    }

    public static TodoResponse from(Todo todo) {
        //null넣는거 별로 안좋은듯?
        return new TodoResponse(List.of(TodoDetail.from(todo)), null);
    }

    public static TodoResponse from(List<Todo> todos) {
        List<TodoDetail> todoList = todos.stream().map(TodoDetail::from).toList();
        return new TodoResponse(todoList, null);
    }
}
