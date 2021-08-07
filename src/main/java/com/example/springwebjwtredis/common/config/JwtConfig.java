package com.example.springwebjwtredis.common.config;

import com.example.springwebjwtredis.common.aop.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtUtil jwtUtil(
            @Value("${spring.application.name}") String issue,
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expired_date}") Long expiredDate
    ) {
        return new JwtUtil(issue, secret, expiredDate);
    }
}
