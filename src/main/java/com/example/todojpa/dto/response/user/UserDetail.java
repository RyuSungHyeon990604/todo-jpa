package com.example.todojpa.dto.response.user;

import com.example.todojpa.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDetail {
    private final Long id;
    private final String email;
    private final String name;

    public static UserDetail  from(User user) {
        return new UserDetail(user.getId(), user.getEmail(), user.getName());
    };

    public static List<UserDetail> from(List<User> users) {
        return users.stream().map(UserDetail::from).toList();
    }
}
