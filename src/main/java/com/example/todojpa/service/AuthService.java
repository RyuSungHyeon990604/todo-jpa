package com.example.todojpa.service;

import com.example.todojpa.dto.request.LoginRequestDto;
import com.example.todojpa.dto.response.TokenResponse;
import com.example.todojpa.entity.User;
import com.example.todojpa.exception.ApplicationException;
import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.repository.UserRepository;
import com.example.todojpa.security.JwtProvider;
import com.example.todojpa.util.PasswordEncoder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public AuthService(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @Transactional
    public String reissueAccessToken(String refreshToken) {
        try {
            Claims validated = jwtProvider.validateToken(refreshToken);
            String userEmail = validated.get("email", String.class);

            User user = userRepository.findByRefreshTokenAndUseYnTrue(refreshToken).orElseThrow(()-> new RuntimeException("User not found"));
            if(userEmail == null || !userEmail.equals(user.getEmail())) {
                throw new ApplicationException(ErrorCode.INVALID_TOKEN);
            }

            return jwtProvider.generateAccessToken(user.getEmail());
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        }

    }

    @Transactional
    public TokenResponse loginJwt(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (PasswordEncoder.matches(password, user.getPassword())) {
            String accessToken = jwtProvider.generateAccessToken(email);

            //로그인 요청시 refreshToken 업데이트 해줘야함
            String refreshToken = jwtProvider.generateRefreshToken(email);
            user.updateToken(refreshToken);

            return new TokenResponse(accessToken, refreshToken);
        } else {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD);
        }

    }


}
