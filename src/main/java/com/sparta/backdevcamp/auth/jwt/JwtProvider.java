package com.sparta.backdevcamp.auth.jwt;

import com.sparta.backdevcamp.auth.entity.TokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import io.jsonwebtoken.security.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.net.http.HttpRequest;
import java.util.Date;
import java.util.UUID;
@Slf4j(topic = "토큰 생성 및 검증")
@Component
public class JwtProvider {
    @Value("${spring.jwt.secret.key}")
    private String SecretKey;
    private SecretKey key;
    private MacAlgorithm algorithm;
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_HEADER = "AccessToken";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    private final long ACCESS_EXPIRATION_TIME = 60 * 60 * 1000L; // 60분
    private final long REFRESH_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일



    @PostConstruct
    public void init(){
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SecretKey));
        algorithm = Jwts.SIG.HS256;
    }

    public TokenPayLoad createTokenPayLoad(String email, TokenType tokenType){
        Date date = new Date();
        long expirationTime = TokenType.ACCESS.equals(tokenType) ? ACCESS_EXPIRATION_TIME : REFRESH_EXPIRATION_TIME;
        return new TokenPayLoad(
                email,
                UUID.randomUUID().toString(),
                date,
                new Date(date.getTime() + expirationTime)
        );
    }

    public String createToken(TokenPayLoad tokenPayLoad){
        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(tokenPayLoad.getSub())
                        .issuedAt(tokenPayLoad.getIat())
                        .expiration(tokenPayLoad.getExpiresAt())
                        .id(tokenPayLoad.getJti())
                        .signWith(key, algorithm)
                        .compact();
    }

    public String getJwtFromHeader(HttpServletRequest request, TokenType tokenType){
        String bearerToken = request.getHeader(TokenType.ACCESS.equals(tokenType) ? ACCESS_TOKEN_HEADER : REFRESH_TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        }
        catch(Exception e){
            log.info(e.getMessage());
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
