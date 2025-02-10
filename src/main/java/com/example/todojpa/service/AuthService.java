package com.example.todojpa.service;

import com.example.todojpa.dto.request.LoginRequestDto;
import com.example.todojpa.dto.response.TokenResponse;
import com.example.todojpa.entity.User;
import com.example.todojpa.entity.UserRefreshToken;
import com.example.todojpa.exception.ApplicationException;
import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.repository.UserRefreshTokenRepository;
import com.example.todojpa.repository.UserRepository;
import com.example.todojpa.security.JwtProvider;
import com.example.todojpa.security.MySecurityContextHolder;
import com.example.todojpa.util.PasswordEncoder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserRefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public AuthService(JwtProvider jwtProvider, UserRefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String reissueAccessToken() {
        try {
            Long userId = getUserIdFromContext();
            UserRefreshToken token = refreshTokenRepository.findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_TOKEN));
            return jwtProvider.generateAccessToken(token.getUserId());
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
        User user = userRepository.findByEmailAndUseYnTrue(email).orElseThrow(()->new ApplicationException(ErrorCode.USER_NOT_FOUND));

        //비밀번호검증
        if (!PasswordEncoder.matches(password, user.getPassword())) {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD);
        }

        //새로운 토큰 생성
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());
        String accessToken = jwtProvider.generateAccessToken(user.getId());


        UserRefreshToken refreshTokenEntity = new UserRefreshToken(user.getId(), refreshToken);
        //토큰을 insert or update 해준다
        refreshTokenRepository.save(refreshTokenEntity);


        return new TokenResponse(accessToken, refreshToken);
    }

    public Long getUserIdFromContext() {
        if(MySecurityContextHolder.getAuthenticated() != null && MySecurityContextHolder.getAuthenticated().getIsValid()) {
            return MySecurityContextHolder.getAuthenticated().getUserId();
        } else {
            throw new ApplicationException(ErrorCode.ACCESS_DENIED);
        }
    }

}
