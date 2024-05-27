package com.sparta.backdevcamp.auth.service.impl;

import com.sparta.backdevcamp.auth.entity.TokenBlackList;
import com.sparta.backdevcamp.auth.entity.TokenType;
import com.sparta.backdevcamp.auth.jwt.JwtProvider;
import com.sparta.backdevcamp.auth.repository.TokenBlackListRepository;
import com.sparta.backdevcamp.auth.service.TokenBlackListService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenBlackListServiceImpl implements TokenBlackListService {
    private final JwtProvider jwtProvider;
    private final TokenBlackListRepository tokenBlackListRepository;
    @Override
    public void addBlackList(String accessToken, String refreshToken) {
        Claims accessInfo = jwtProvider.getUserInfoFromToken(accessToken);
        Claims refreshInfo = jwtProvider.getUserInfoFromToken(refreshToken);

        tokenBlackListRepository.save(new TokenBlackList(
                accessToken,
                accessInfo.getId(),
                TokenType.ACCESS,
                accessInfo.getExpiration()
        ));

        tokenBlackListRepository.save(new TokenBlackList(
                refreshToken,
                refreshInfo.getId(),
                TokenType.REFRESH,
                refreshInfo.getExpiration()
        ));

    }

    @Override
    public boolean isTokenBlackListed(String jti) {
        Optional<TokenBlackList> tokenBlackList = tokenBlackListRepository.findByJti(jti);
        return tokenBlackList.isPresent();
    }

    @Override
    public void removeExpiredToken() {
        List<TokenBlackList> tokenBlackLists = tokenBlackListRepository.findAllByExpiresAtLessThan(new Date());
        tokenBlackListRepository.deleteAllInBatch(tokenBlackLists);
    }
}
