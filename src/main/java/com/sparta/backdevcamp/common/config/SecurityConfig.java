package com.sparta.backdevcamp.common.config;

import com.sparta.backdevcamp.auth.jwt.JwtProvider;
import com.sparta.backdevcamp.auth.repository.AccessLogRepository;
import com.sparta.backdevcamp.common.security.JwtAuthenticationFilter;
import com.sparta.backdevcamp.common.security.JwtAuthorizationFilter;
import com.sparta.backdevcamp.common.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsServiceImpl userDetailsService;
    private final AccessLogRepository accessLogRepository;


    /**
     * Security에서 제공하는 비밀번호 암호화 인터페이스 구현체를 Bean으로 등록
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Security 인증객체들을 괸리하는 Bean 등록
     * @param configuration : Security 인증관련 설정 Bean
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider,accessLogRepository);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter(jwtProvider, userDetailsService);
    }

    /**
     * Security애서 인증/인가 제어를 위해 관리하는 Filter 설정
     * @param security : SecurityFilterChain 구현체
     * @return HttpSecurity
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        // CSRF(사이트 간 요청 위조) 설정 비활성화 : B/C 세션 방식이 아닌 JWT 방식을 사용
        security.csrf((csrf) -> csrf.disable());
        security.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        // API 제어설정
        security.authorizeHttpRequests((request)->
                request.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
        );

        // Security 의 기본 설정인 Session 방식이 아닌 JWT 방식을 사용하기 위한 설정
        security.sessionManagement((sessionManagement)-> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // JWT 방식의 REST API 서버이기 때문에 formlogin, httpbasic 비활성화
        security.formLogin((formlogin) -> formlogin.disable())
                .httpBasic((httpbasic) -> httpbasic.disable());


        // JWT 필터 등록
        security.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        security.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }

}
