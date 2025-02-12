package com.example.todojpa.controller;


import com.example.todojpa.annotation.DateCheck;
import com.example.todojpa.dto.request.todo.TodoCreateRequestDto;
import com.example.todojpa.dto.response.todo.TodoResponse;
import com.example.todojpa.dto.request.todo.TodoUpdateRequestDto;
import com.example.todojpa.service.TodoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@RestController
@Validated
@RequestMapping("/todos")//복수형으로 작성하기
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("")
    public ResponseEntity<TodoResponse> search(@RequestParam(value = "username", required = false) String userName,
                                               @RequestParam(value = "date", required = false) @DateCheck String date,
                                               @RequestParam(value = "page", required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                               @RequestParam(value = "size", required = false, defaultValue = "10") @Positive Integer size) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate inputDate = null;
        if(date != null) {
            inputDate = LocalDate.parse(date, formatter);
        }
        TodoResponse todos = todoService.search(userName, inputDate, PageRequest.of(page, size));

        return ResponseEntity.ok(todos);
    }

    //n+1발생
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> findTodoById(@PathVariable Long id) {
        TodoResponse todos = todoService.findTodoById(id);
        return ResponseEntity.ok(todos);
    }

    @PostMapping("")
    public ResponseEntity<TodoResponse> createTodo(@RequestBody @Valid TodoCreateRequestDto requestDto) {
        TodoResponse todo = todoService.createTodo(requestDto);
        return ResponseEntity.ok(todo);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateTodo(@PathVariable Long id,
                                           @RequestBody @Valid TodoUpdateRequestDto requestDto) {
        todoService.updateTodo(requestDto,id);
        return ResponseEntity.noContent().build();
    }

    //post delete, soft delete 써보기
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}
