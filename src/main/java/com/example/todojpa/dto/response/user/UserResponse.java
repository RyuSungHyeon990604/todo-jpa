package com.example.todojpa.dto.response.user;

import lombok.Getter;

import java.util.List;

@Getter
public class UserResponse {
    private final List<UserDetail> data;

    public UserResponse(List<UserDetail> data) {
        this.data = data;
    }
    public UserResponse(UserDetail data) {
        this.data = List.of(data);
    }

}
