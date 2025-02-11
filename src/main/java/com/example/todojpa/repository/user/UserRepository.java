package com.example.todojpa.repository.user;

import com.example.todojpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    List<User> findAllByName(String name);
    Optional<User> findByEmail(String email);
}
