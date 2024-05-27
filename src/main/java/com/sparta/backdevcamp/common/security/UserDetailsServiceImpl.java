package com.sparta.backdevcamp.common.security;

import com.sparta.backdevcamp.auth.entity.User;
import com.sparta.backdevcamp.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       User user = userRepository.findByEmail(email).orElseThrow(()->
               new UsernameNotFoundException("User Not Found"));

       return new UserDetailsImpl(user);
    }
}
