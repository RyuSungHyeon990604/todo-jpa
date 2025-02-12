package com.example.todojpa.service;

import com.example.todojpa.dto.request.todo.TodoCreateRequestDto;
import com.example.todojpa.dto.request.todo.TodoUpdateRequestDto;
import com.example.todojpa.dto.response.todo.TodoResponse;
import com.example.todojpa.entity.Todo;
import com.example.todojpa.entity.User;
import com.example.todojpa.exception.ApplicationException;
import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.repository.todo.TodoRepository;
import com.example.todojpa.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public TodoResponse search(String userName, LocalDate date, Pageable page) {
        Page<Todo> todos = todoRepository.search(userName, date, page);

        return TodoResponse.from(todos);
    }

    @Transactional(readOnly = true)
    public TodoResponse findTodoById(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.TODO_NOT_FOUND));

        return TodoResponse.from(todo);
    }

    @Transactional
    public TodoResponse createTodo(Long loginUserId, TodoCreateRequestDto requestDto) {
        User user = userRepository.findById(loginUserId).orElseThrow(() -> new ApplicationException(ErrorCode.TODO_NOT_FOUND));

        Todo todo = Todo.builder()
                .task(requestDto.getTask())
                .title(requestDto.getTitle())
                .user(user).build();

        Todo save = todoRepository.save(todo);

        return TodoResponse.from(save);
    }

    @Transactional
    public void updateTodo(Long todoId, Long loginUserId, TodoUpdateRequestDto requestDto){
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new ApplicationException(ErrorCode.TODO_NOT_FOUND));

        if(!todo.getUser().getId().equals(loginUserId)) {
            throw new ApplicationException(ErrorCode.ACCESS_DENIED);
        }

        todo.update(requestDto.getTitle(), requestDto.getTask());
    }

    @Transactional
    public void deleteTodo(Long id, Long loginUserId) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.TODO_NOT_FOUND));

        if(!todo.getUser().getId().equals(loginUserId)) {
            throw new ApplicationException(ErrorCode.ACCESS_DENIED);
        }

        todoRepository.delete(todo);
    }


}
