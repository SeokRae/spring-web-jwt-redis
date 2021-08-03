package com.example.springwebjwtredis.config;

import com.example.springwebjwtredis.aop.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value(value = "${spring.application.name}")
    private String issue;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expired_date}")
    private Long expiredDate;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(issue, secret, expiredDate);
    }
}
