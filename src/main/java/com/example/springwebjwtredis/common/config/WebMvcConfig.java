package com.example.springwebjwtredis.common.config;

import com.example.springwebjwtredis.access.repository.AccessTokenRepository;
import com.example.springwebjwtredis.common.aop.auth.AuthenticationInterceptor;
import com.example.springwebjwtredis.common.aop.auth.AuthorizationInterceptor;
import com.example.springwebjwtredis.common.aop.jwt.JwtUtil;
import com.example.springwebjwtredis.member.service.MemberService;
import com.example.springwebjwtredis.refresh.service.RefreshTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public WebMvcConfig(AccessTokenRepository customRedisRepository, RefreshTokenService refreshTokenService, MemberService memberService, JwtUtil jwtUtil) {
        this.accessTokenRepository = customRedisRepository;
        this.refreshTokenService = refreshTokenService;
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }


    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor(jwtUtil, memberService, accessTokenRepository, refreshTokenService);
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor() {
        return new AuthorizationInterceptor(accessTokenRepository, jwtUtil);
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
