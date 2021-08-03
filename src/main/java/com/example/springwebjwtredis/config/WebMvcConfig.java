package com.example.springwebjwtredis.config;

import com.example.springwebjwtredis.aop.auth.AuthenticationInterceptor;
import com.example.springwebjwtredis.aop.auth.AuthorizationInterceptor;
import com.example.springwebjwtredis.aop.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;

    public WebMvcConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor(jwtUtil);
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor() {
        return new AuthorizationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(authenticationInterceptor())
                .addPathPatterns(
                        "/login"
                )
                .excludePathPatterns(
                        "/members*"
                );

//        registry
//                .addInterceptor(authorizationInterceptor())
//                .addPathPatterns(
//                        "/members/**"
//                );
    }
}
