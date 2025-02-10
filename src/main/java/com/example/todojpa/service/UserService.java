package com.example.todojpa.service;

import com.example.todojpa.dto.request.UserCreateRequestDto;
import com.example.todojpa.dto.request.UserDeleteRequestDto;
import com.example.todojpa.dto.response.UserDetail;
import com.example.todojpa.dto.response.UserResponse;
import com.example.todojpa.dto.request.UserUpdateRequestDto;
import com.example.todojpa.entity.User;
import com.example.todojpa.exception.ApplicationException;
import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.repository.comment.CommentRepository;
import com.example.todojpa.repository.todo.TodoRepository;
import com.example.todojpa.repository.user.UserRepository;
import com.example.todojpa.security.JwtProvider;
import com.example.todojpa.security.MySecurityContextHolder;
import com.example.todojpa.util.PasswordEncoder;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    public UserService(UserRepository userRepository, TodoRepository todoRepository, CommentRepository commentRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        return new UserResponse(UserDetail.from(user));
    }

    @Transactional
    public UserResponse search(String name) {
        List<User> users = userRepository.findAllByName(name);

        return new UserResponse(UserDetail.from(users));
    }

    @Transactional
    public UserResponse createUser(UserCreateRequestDto requestDto) {
        Optional<User> duplicateCheck = userRepository.findByEmail(requestDto.getEmail());
        if(duplicateCheck.isPresent()){
            throw new ApplicationException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User user = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(PasswordEncoder.encode(requestDto.getPassword()))
                .build();

        User save = userRepository.save(user);

        return new UserResponse(UserDetail.from(save));
    }

    @Transactional
    public void deleteUser(Long userId, UserDeleteRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        Long id = getUserIdFromContext();

        if(!id.equals(user.getId())) {
            throw new ApplicationException(ErrorCode.ACCESS_DENIED);
        }
        if(!requestDto.getPassword().equals(user.getPassword())) {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD);
        }

        commentRepository.softDeleteByUserId(user.getId());
        todoRepository.softDeleteByUserId(user.getId());
        user.softDelete();
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        Long id = getUserIdFromContext();

        if(!id.equals(user.getId())) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        }
        if(!PasswordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD);
        }

        user.updateUser(requestDto.getName(), requestDto.getEmail(), requestDto.getPassword());
    }

    public Long getUserIdFromContext() {
        if(MySecurityContextHolder.getAuthenticated() != null && MySecurityContextHolder.getAuthenticated().getIsValid()) {
            return MySecurityContextHolder.getAuthenticated().getUserId();
        } else {
            throw new ApplicationException(ErrorCode.ACCESS_DENIED);
        }
    }
}
