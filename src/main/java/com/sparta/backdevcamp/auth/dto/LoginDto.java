package com.sparta.backdevcamp.auth.dto;

import lombok.Getter;

public class LoginDto {
    @Getter
    public static class LoginRequestDto{
        private String email;
        private String password;
    }
}
