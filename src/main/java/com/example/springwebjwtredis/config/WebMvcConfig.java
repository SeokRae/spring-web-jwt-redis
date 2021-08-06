package com.example.springwebjwtredis.config;

import com.example.springwebjwtredis.aop.auth.AuthenticationInterceptor;
import com.example.springwebjwtredis.aop.auth.AuthorizationInterceptor;
import com.example.springwebjwtredis.aop.jwt.JwtUtil;
import com.example.springwebjwtredis.redis.repository.CustomRedisRepository;
import com.example.springwebjwtredis.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private final CustomRedisRepository redisRepository;

    public WebMvcConfig(JwtUtil jwtUtil, MemberService memberService, CustomRedisRepository redisRepository) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
        this.redisRepository = redisRepository;
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor(jwtUtil, memberService, redisRepository);
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor() {
        return new AuthorizationInterceptor(jwtUtil, redisRepository);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(authenticationInterceptor())
                .addPathPatterns(
                        "/auth/**"
                );

        registry
                .addInterceptor(authorizationInterceptor())
                .addPathPatterns(
                        "/members/**"
                )
                .excludePathPatterns(
                        "/members"
                );
    }
}
