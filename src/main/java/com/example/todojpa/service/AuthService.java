package com.example.todojpa.service;

import com.example.todojpa.dto.request.login.LoginRequestDto;
import com.example.todojpa.dto.response.token.TokenResponse;
import com.example.todojpa.entity.User;
import com.example.todojpa.entity.UserRefreshToken;
import com.example.todojpa.exception.ApplicationException;
import com.example.todojpa.exception.ErrorCode;
import com.example.todojpa.repository.refreshToken.UserRefreshTokenRepository;
import com.example.todojpa.repository.user.UserRepository;
import com.example.todojpa.security.JwtProvider;
import com.example.todojpa.security.MySecurityContextHolder;
import com.example.todojpa.util.PasswordEncoder;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public TokenResponse reissueAccessToken() {
        try {
            //인증정보로 refresh이 존재하는지 확인
            Long userId = getUserIdFromContext();
            UserRefreshToken token = refreshTokenRepository.findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_TOKEN));

            //토큰 재발행
            String refreshToken = jwtProvider.generateRefreshToken(userId);
            String accessToken = jwtProvider.generateAccessToken(userId);

            //refresh 토큰은 업데이트
            token.updateRefreshToken(refreshToken);

            return new TokenResponse(accessToken,  refreshToken);
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
        User user = userRepository.findByEmail(email).orElseThrow(()->new ApplicationException(ErrorCode.USER_NOT_FOUND));

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

    @Transactional
    public void logOut(Long userId) {
        //refresh token을 삭제하고
        refreshTokenRepository.deleteById(userId);

        //사용자의 로그아웃시간을 갱신
        User user = userRepository.findById(userId).orElseThrow(()->new ApplicationException(ErrorCode.USER_NOT_FOUND));
        user.updateLogoutTime(LocalDateTime.now());
    }

    public Long getUserIdFromContext() {
        if(MySecurityContextHolder.getAuthenticated() != null && MySecurityContextHolder.getAuthenticated().getIsValid()) {
            return MySecurityContextHolder.getAuthenticated().getUserId();
        } else {
            throw new ApplicationException(ErrorCode.ACCESS_DENIED);
        }
    }


}
