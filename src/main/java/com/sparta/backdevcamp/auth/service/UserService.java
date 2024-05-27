package com.sparta.backdevcamp.auth.service;

import com.sparta.backdevcamp.auth.dto.SignupDto;

public interface UserService {
    /**
     * 회원가입 API
     * @param requestDto : email, password, username
     * @return SignupDto.ResponseDto
     */
    SignupDto.ResponseDto signup(SignupDto.RequestDto requestDto);

}
