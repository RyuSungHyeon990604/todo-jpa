package com.example.todojpa.repository;

import com.example.todojpa.entity.User;

import java.util.List;

public interface CustomUserRepository  {
    List<User> search(String name);
}
