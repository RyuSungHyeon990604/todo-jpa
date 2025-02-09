package com.example.todojpa.repository;

import com.example.todojpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndUseYnTrue(Long id);
    Optional<User> findByRefreshTokenAndUseYnTrue(String refreshToken);
    List<User> findAllByName(String name);
}
