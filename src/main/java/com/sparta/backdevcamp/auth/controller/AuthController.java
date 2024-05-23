package com.sparta.backdevcamp.auth.controller;

import com.sparta.backdevcamp.auth.dto.SignupDto;
import com.sparta.backdevcamp.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<SignupDto.ResponseDto> signup(@RequestBody SignupDto.RequestDto requestDto){
        SignupDto.ResponseDto responseDto= userService.signup(requestDto);
        return ResponseEntity.ok(responseDto);

    }

}
