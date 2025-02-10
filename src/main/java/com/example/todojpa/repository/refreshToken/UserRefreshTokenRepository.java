package com.example.todojpa.repository.refreshToken;

import com.example.todojpa.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
}
