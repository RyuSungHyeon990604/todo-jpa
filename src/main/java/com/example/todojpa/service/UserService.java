package com.example.todojpa.service;

import com.example.todojpa.dto.request.user.UserCreateRequestDto;
import com.example.todojpa.dto.request.user.UserDeleteRequestDto;
import com.example.todojpa.dto.response.user.UserDetail;
import com.example.todojpa.dto.response.user.UserResponse;
import com.example.todojpa.dto.request.user.UserUpdateRequestDto;
import com.example.todojpa.entity.User;
import com.example.todojpa.exception.ApplicationException;
import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.repository.user.UserRepository;
import com.example.todojpa.security.MySecurityContextHolder;
import com.example.todojpa.util.PasswordEncoder;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        Optional<User> duplicateCheck = userRepository.findByEmailForCheckDuplicate(requestDto.getEmail());
        if (duplicateCheck.isPresent()) {
            User user = duplicateCheck.get();
            if(user.getDeleted()){ //탈퇴했던 사용자라면
                //탈퇴한 회원임을 알리기?
                throw new ApplicationException(ErrorCode.USER_ACCOUNT_DELETED);
            }
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
    public void deleteUser(Long userId, Long loginUserId, UserDeleteRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        if(!loginUserId.equals(user.getId())) {
            throw new ApplicationException(ErrorCode.ACCESS_DENIED);
        }
        if(!requestDto.getPassword().equals(user.getPassword())) {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD);
        }

        userRepository.delete(user);
    }

    @Transactional
    public void updateUser(Long userId, Long loginUserId, UserUpdateRequestDto requestDto) {
        if(!loginUserId.equals(userId)) {
            throw new ApplicationException(ErrorCode.ACCESS_DENIED);
        }
        User user = userRepository.findById(loginUserId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));


        if(!loginUserId.equals(user.getId())) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        }
        if(!PasswordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD);
        }

        user.updateUser(requestDto.getName(), requestDto.getEmail(), requestDto.getPassword());
    }
}
