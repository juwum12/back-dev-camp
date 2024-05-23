package com.sparta.backdevcamp.auth.dto;

import com.sparta.backdevcamp.auth.entity.UserRole;
import lombok.Getter;

public class SignupDto {

    @Getter
    public static class ResponseDto{
        private final Long id;
        private final String email;
        private final String username;

        public ResponseDto(Long id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }
    }
    @Getter
    public static class RequestDto{
        private String email;
        private String password;
        private String username;
        private UserRole role = UserRole.USER;

    }
}
