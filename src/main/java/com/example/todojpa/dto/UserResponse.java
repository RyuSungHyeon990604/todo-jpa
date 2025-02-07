package com.example.todojpa.dto;

import com.example.todojpa.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {
    String name;
    String email;

    public static UserResponse from(User user) {
        return new UserResponse(user.getName(), user.getEmail());
    }
}
