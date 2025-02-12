package com.example.todojpa.repository.todo;

import com.example.todojpa.dto.response.todo.TodoDetail;
import com.example.todojpa.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CustomTodoRepository {

    Page<TodoDetail> search(String userName, LocalDate updatedAt, Pageable pageable);
}
