package com.example.todojpa.service;

import com.example.todojpa.dto.request.TodoCreateRequestDto;
import com.example.todojpa.dto.request.TodoUpdateRequestDto;
import com.example.todojpa.dto.response.TodoResponse;
import com.example.todojpa.entity.Todo;
import com.example.todojpa.entity.User;
import com.example.todojpa.repository.CommentRepository;
import com.example.todojpa.repository.TodoRepository;
import com.example.todojpa.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public TodoResponse search(String userName, LocalDate date, Pageable page) {
        Page<Todo> todos = todoRepository.search(userName, date, page);

        return TodoResponse.from(todos);
    }

    @Transactional(readOnly = true)
    public TodoResponse findTodoById(Long id) {

        Todo todo = todoRepository.findByIdAndUseYnTrue(id).orElseThrow(() -> new RuntimeException("Todo Not Found"));

        return TodoResponse.from(todo);
    }

    @Transactional
    public TodoResponse createTodo(TodoCreateRequestDto requestDto, Long userId) {

        User user = userRepository.findByIdAndUseYnTrue(userId).orElseThrow(() -> new RuntimeException("User Not Found"));

        Todo todo = Todo.builder()
                .task(requestDto.getTask())
                .title(requestDto.getTitle())
                .user(user).build();

        Todo save = todoRepository.save(todo);

        return TodoResponse.from(save);
    }

    @Transactional
    public void updateTodo(TodoUpdateRequestDto requestDto, Long todoId , Long userId){
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new RuntimeException("Todo Not Found"));

        if(!todo.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한 없음");
        }

        todo.update(requestDto.getTitle(), requestDto.getTask());
    }

    @Transactional
    public void deleteTodo(Long id, Long userId) {
        Todo todo = todoRepository.findByIdAndUseYnTrue(id).orElseThrow(() -> new RuntimeException("Todo Not Found"));

        if(!todo.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한 없음");
        }

        commentRepository.softDeleteByTodoId(todo.getId());
        todo.softDelete();
    }
}
