package com.example.todojpa.dto.response.todo;

import com.example.todojpa.dto.response.PageInfo;
import com.example.todojpa.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class TodoResponse {
    private final List<TodoDetail> data;
    private final PageInfo pageInfo;


    public static TodoResponse from(Page<TodoDetail> data) {
        return new TodoResponse(data.getContent(),new PageInfo(data));
    }

    public static TodoResponse from(List<TodoDetail> data) {
        return new TodoResponse(data,null);
    }

    public static TodoResponse from(TodoDetail data) {
        return new TodoResponse(List.of(data),null);
    }
}
