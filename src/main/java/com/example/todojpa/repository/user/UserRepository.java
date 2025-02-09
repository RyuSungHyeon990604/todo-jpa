package com.example.todojpa.repository.user;

import com.example.todojpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    Optional<User> findByIdAndUseYnTrue(Long id);
    List<User> findAllByName(String name);
    Optional<User> findByEmailAndUseYnTrue(String name);
    Optional<User> findByEmail(String email);
}
