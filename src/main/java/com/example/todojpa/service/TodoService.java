package com.example.todojpa.service;

import com.example.todojpa.dto.request.todo.TodoCreateRequestDto;
import com.example.todojpa.dto.request.todo.TodoUpdateRequestDto;
import com.example.todojpa.dto.response.comment.CommentDetail;
import com.example.todojpa.dto.response.todo.TodoDetail;
import com.example.todojpa.dto.response.todo.TodoResponse;
import com.example.todojpa.entity.Comment;
import com.example.todojpa.entity.Todo;
import com.example.todojpa.entity.User;
import com.example.todojpa.exception.ApplicationException;
import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.repository.comment.CommentRepository;
import com.example.todojpa.repository.todo.TodoRepository;
import com.example.todojpa.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


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
        Page<TodoDetail> todos = todoRepository.search(userName, date, page);

        return TodoResponse.from(todos);
    }

    @Transactional(readOnly = true)
    public TodoResponse findTodoById(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.TODO_NOT_FOUND));
        List<Comment> comments = commentRepository.findAllByTodoIdOrderByLevelAscCreatedAtDesc(todo.getId());

        TodoDetail dto = TodoDetail.from(todo);
        dto.setComments(CommentDetail.convertTree(comments));

        return TodoResponse.from(dto);
    }

    @Transactional
    public TodoResponse createTodo(Long loginUserId, TodoCreateRequestDto requestDto) {
        User user = userRepository.findById(loginUserId).orElseThrow(() -> new ApplicationException(ErrorCode.TODO_NOT_FOUND));

        Todo todo = Todo.builder()
                .task(requestDto.getTask())
                .title(requestDto.getTitle())
                .user(user).build();

        Todo save = todoRepository.save(todo);

        return TodoResponse.from(TodoDetail.from(save));
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
