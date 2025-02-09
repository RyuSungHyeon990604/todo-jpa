package com.example.todojpa.dto.response;

import com.example.todojpa.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDetail {
    private Long id;
    private String email;
    private String name;

    public static UserDetail  from(User user) {
        return new UserDetail(user.getId(), user.getEmail(), user.getName());
    };

    public static List<UserDetail> from(List<User> users) {
        return users.stream().map(UserDetail::from).toList();
    }
}
