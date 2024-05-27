package com.sparta.backdevcamp.auth.service.impl;

import com.sparta.backdevcamp.auth.dto.SignupDto;
import com.sparta.backdevcamp.auth.entity.User;
import com.sparta.backdevcamp.auth.repository.UserRepository.UserRepository;
import com.sparta.backdevcamp.auth.service.UserService;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupDto.ResponseDto signup(SignupDto.RequestDto requestDto){
        Optional<User> existUser = userRepository.findByEmail(requestDto.getEmail());
        if(existUser.isPresent()){
            throw new IllegalArgumentException(requestDto.getEmail() + " 은 이미 존재하는 이메일입니다.");
        }
        User user = new User(
                requestDto.getEmail(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getUsername(),
                requestDto.getRole()
        );

        userRepository.save(user);

        return new SignupDto.ResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
                );
    }
}
