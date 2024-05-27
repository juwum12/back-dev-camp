package com.sparta.backdevcamp.auth.controller;

import com.sparta.backdevcamp.auth.dto.SignupDto;
import com.sparta.backdevcamp.auth.entity.TokenType;
import com.sparta.backdevcamp.auth.jwt.JwtProvider;
import com.sparta.backdevcamp.auth.service.AuthService;
import com.sparta.backdevcamp.auth.service.TokenBlackListService;
import com.sparta.backdevcamp.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final TokenBlackListService tokenBlackListService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupDto.ResponseDto> signup(@RequestBody SignupDto.RequestDto requestDto){
        SignupDto.ResponseDto responseDto= userService.signup(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){
        tokenBlackListService.addBlackList(
                jwtProvider.getJwtFromHeader(request, TokenType.ACCESS),
                jwtProvider.getJwtFromHeader(request, TokenType.REFRESH)
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request){
        String accessToken = authService.refreshAccessToken(jwtProvider.getJwtFromHeader(request, TokenType.REFRESH));
        return ResponseEntity.ok(accessToken);
    }

    @DeleteMapping("/blacklist")
    public ResponseEntity<Void> blacklist(){
        tokenBlackListService.removeExpiredToken();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
