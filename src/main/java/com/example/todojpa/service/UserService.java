package com.example.todojpa.service;

import com.example.todojpa.dto.request.UserCreateRequestDto;
import com.example.todojpa.dto.request.UserDeleteRequestDto;
import com.example.todojpa.dto.response.UserDetail;
import com.example.todojpa.dto.response.UserResponse;
import com.example.todojpa.dto.request.UserUpdateRequestDto;
import com.example.todojpa.entity.User;
import com.example.todojpa.exception.ApplicationException;
import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.repository.CommentRepository;
import com.example.todojpa.repository.TodoRepository;
import com.example.todojpa.repository.UserRepository;
import com.example.todojpa.security.JwtProvider;
import com.example.todojpa.util.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, TodoRepository todoRepository, CommentRepository commentRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.commentRepository = commentRepository;
        this.jwtProvider = jwtProvider;
    }

    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        return new UserResponse(UserDetail.from(user));
    }

    public UserResponse search(String name) {
        List<User> users = userRepository.findAllByName(name);

        return new UserResponse(UserDetail.from(users));
    }

    public UserResponse createUser(UserCreateRequestDto requestDto) {
        String refreshToken = jwtProvider.generateRefreshToken(requestDto.getEmail());

        User user = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(PasswordEncoder.encode(requestDto.getPassword()))
                .refreshToken(refreshToken)
                .build();

        User save = userRepository.save(user);

        return new UserResponse(UserDetail.from(save));
    }

    public void deleteUser(Long userId, UserDeleteRequestDto requestDto, String accessToken) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        String userEmailFromToken = jwtProvider.getUserEmail(accessToken);

        if(!userEmailFromToken.equals(user.getEmail())) {
            throw new ApplicationException(ErrorCode.ACCESS_DENIED);
        }
        if(!requestDto.getPassword().equals(user.getPassword())) {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD);
        }

        commentRepository.softDeleteByUserId(user.getId());
        todoRepository.softDeleteByUserId(user.getId());
        user.softDelete();
    }

    public void updateUser(Long userId, UserUpdateRequestDto requestDto, String accessToken) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        String emailFromToken = jwtProvider.getUserEmail(accessToken);

        if(!emailFromToken.equals(user.getEmail())) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        }
        if(!requestDto.getPassword().equals(user.getPassword())) {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD);
        }

        user.updateUser(requestDto.getName(), requestDto.getEmail(), requestDto.getPassword());
    }
}
