package com.example.todojpa.repository.todo;

import com.example.todojpa.dto.response.todo.TodoDetail;
import com.example.todojpa.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, CustomTodoRepository {
}
