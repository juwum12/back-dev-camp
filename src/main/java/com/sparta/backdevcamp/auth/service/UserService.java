package com.sparta.backdevcamp.auth.service;

import com.sparta.backdevcamp.auth.dto.SignupDto;

public interface UserService {
    SignupDto.ResponseDto signup(SignupDto.RequestDto requestDto);

}
