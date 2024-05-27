package com.sparta.backdevcamp.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.backdevcamp.auth.dto.LoginDto;
import com.sparta.backdevcamp.auth.entity.TokenType;
import com.sparta.backdevcamp.auth.entity.User;
import com.sparta.backdevcamp.auth.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtProvider jwtProvider;
    public JwtAuthenticationFilter(JwtProvider jwtProvider){
        this.jwtProvider = jwtProvider;
        setFilterProcessesUrl("/auth/login");
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            LoginDto.LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginDto.LoginRequestDto.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        }
        catch(IOException e){
            log.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user  = ((UserDetailsImpl) authResult.getPrincipal()).getUser();

        String accessToken = jwtProvider.createToken(jwtProvider.createTokenPayLoad(user.getEmail(), TokenType.ACCESS));
        String refreshToken = jwtProvider.createToken(jwtProvider.createTokenPayLoad(user.getEmail(), TokenType.REFRESH));

        log.info("AccessToken : " + accessToken );
        log.info("RefreshToken  : " + refreshToken);

        response.addHeader(JwtProvider.ACCESS_TOKEN_HEADER, accessToken);
        response.addHeader(JwtProvider.REFRESH_TOKEN_HEADER, refreshToken);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
