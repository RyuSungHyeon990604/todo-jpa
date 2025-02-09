package com.example.todojpa.dto.response;

import com.example.todojpa.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserResponse {
    List<UserDetail> data;

    public UserResponse(List<UserDetail> data) {
        this.data = data;
    }
    public UserResponse(UserDetail data) {
        this.data = List.of(data);
    }

}
