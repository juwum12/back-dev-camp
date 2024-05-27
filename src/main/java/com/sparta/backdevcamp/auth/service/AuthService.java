package com.sparta.backdevcamp.auth.service;

import org.springframework.stereotype.Service;

public interface AuthService {
    /**
     * Refresh Token 의 유효성 검사 후 Access Token 발급
     * @param refreshToken Http Request Header 에서 추출한 토큰
     * @return Access Token
     */
    public String refreshAccessToken(String refreshToken);
}
