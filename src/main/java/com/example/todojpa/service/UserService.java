package com.example.todojpa.service;

import com.example.todojpa.dto.UserCreateRequestDto;
import com.example.todojpa.dto.UserResponse;
import com.example.todojpa.dto.UserUpdateRequestDto;
import com.example.todojpa.entity.User;
import com.example.todojpa.repository.CommentRepository;
import com.example.todojpa.repository.TodoRepository;
import com.example.todojpa.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    public UserService(UserRepository userRepository, TodoRepository todoRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.commentRepository = commentRepository;
    }

    public UserResponse findUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        return UserResponse.from(user);
    }

    public List<UserResponse> search(String name) {
        List<User> users = userRepository.findAllByName(name);

        return users.stream().map(UserResponse::from).toList();
    }

    public UserResponse createUser(UserCreateRequestDto requestDto) {
        User user = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .build();

        User save = userRepository.save(user);

        return UserResponse.from(save);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));

        commentRepository.softDeleteByUserId(user.getId());
        todoRepository.softDeleteByUserId(user.getId());
        user.softDelete();
    }

    public void updateUser(Long userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.updateUser(requestDto.getName(), requestDto.getEmail(), requestDto.getPassword());
    }
}
