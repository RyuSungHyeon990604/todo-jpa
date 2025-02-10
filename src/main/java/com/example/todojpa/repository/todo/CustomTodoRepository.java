package com.example.todojpa.repository.todo;

import com.example.todojpa.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CustomTodoRepository {

    Page<Todo> search(String userName, LocalDate updatedAt, Pageable pageable);
}
