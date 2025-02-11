package com.example.todojpa.repository.todo;

import com.example.todojpa.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, CustomTodoRepository {
}
