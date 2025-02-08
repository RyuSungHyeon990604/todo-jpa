package com.example.todojpa.controller;


import com.example.todojpa.dto.request.TodoCreateRequestDto;
import com.example.todojpa.dto.response.TodoResponse;
import com.example.todojpa.dto.request.TodoUpdateRequestDto;
import com.example.todojpa.service.TodoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/todos")//복수형으로 작성하기
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("")
    public ResponseEntity<TodoResponse> search(@RequestParam(value = "username", required = false) String userName,
                                               @RequestParam(value = "date", required = false) LocalDate date,
                                               @RequestParam(value = "page", required = false) Integer page,
                                               @RequestParam(value = "size", required = false) Integer size) {
        TodoResponse todos = todoService.search(userName, date, PageRequest.of(page, size));

        return ResponseEntity.ok(todos);
    }

    //n+1발생
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> findTodoById(@PathVariable Long id) {
        TodoResponse todos = todoService.findTodoById(id);
        return ResponseEntity.ok(todos);
    }

    @PostMapping("")
    public ResponseEntity<TodoResponse> createTodo(@RequestBody TodoCreateRequestDto requestDto,
                                                   @RequestHeader Long userId) {
        TodoResponse todo = todoService.createTodo(requestDto, userId);
        return ResponseEntity.ok(todo);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateTodo(@PathVariable Long id,
                                           @RequestBody TodoUpdateRequestDto requestDto,
                                           @RequestHeader Long userId) {
        todoService.updateTodo(requestDto,id,userId);
        return ResponseEntity.noContent().build();
    }

    //post delete, soft delete 써보기
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, @RequestHeader Long userId) {
        todoService.deleteTodo(id, userId);
        return ResponseEntity.noContent().build();
    }
}
