package com.sparta.backdevcamp.auth.service.impl;

import com.sparta.backdevcamp.auth.entity.TokenType;
import com.sparta.backdevcamp.auth.entity.User;
import com.sparta.backdevcamp.auth.jwt.JwtProvider;
import com.sparta.backdevcamp.auth.repository.UserRepository;
import com.sparta.backdevcamp.auth.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public String refreshAccessToken(String refreshToken) {
        if(!jwtProvider.validateToken(refreshToken)){
            throw new RuntimeException("Invalid Token");
        }
        // Jwt Claims
        Claims info  = jwtProvider.getUserInfoFromToken(refreshToken);

        // User 조회
        User user = userRepository.findByEmail(info.getSubject()).orElseThrow(()->
                new RuntimeException("Not Found User By : " + info.getSubject()));
        return jwtProvider.createToken(jwtProvider.createTokenPayLoad(user.getEmail(), TokenType.ACCESS));
    }
}
