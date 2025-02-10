package com.example.todojpa.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtProvider {

    private final Key key;
    private final long ACCESS_EXPIRATION_TIME = 1000*60*5;
    private final long REFRESH_EXPIRATION_TIME = 1000*60*60*24;

    public JwtProvider(@Value("${jwt.secret}")String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long userId) {
        long now = new Date().getTime();

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> payLoad = new HashMap<>();
        payLoad.put("userId", userId);
        payLoad.put("type", "access");

        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payLoad)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ACCESS_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        long now = new Date().getTime();

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> payLoad = new HashMap<>();
        payLoad.put("userId", userId);
        payLoad.put("type", "refresh");

        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payLoad)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + REFRESH_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserId(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }

}
