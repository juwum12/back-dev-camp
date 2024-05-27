package com.sparta.backdevcamp.common.security;

import com.sparta.backdevcamp.auth.entity.TokenType;
import com.sparta.backdevcamp.auth.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtProvider.getJwtFromHeader(request, TokenType.ACCESS);
        String refreshToken = jwtProvider.getJwtFromHeader(request, TokenType.REFRESH);
        if(StringUtils.hasText(accessToken)){
            if(!jwtProvider.validateToken(accessToken)){
                log.info("AccessToken Error");
                return;
            }
            Claims info = jwtProvider.getUserInfoFromToken(accessToken);
            try{
                setAuthentication(info.getSubject());
            }
            catch(Exception e){
                log.info(e.getMessage());
                return;
            }
        }
        else if (StringUtils.hasText(refreshToken)){
            if(!jwtProvider.validateToken(refreshToken)){
                log.info("refreshToken Error");
                return;
            }
            Claims info = jwtProvider.getUserInfoFromToken(refreshToken);
            try{
                setAuthentication(info.getSubject());
            }
            catch(Exception e){
                log.info(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request,response);
    }

    private void setAuthentication(String email) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);

        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
