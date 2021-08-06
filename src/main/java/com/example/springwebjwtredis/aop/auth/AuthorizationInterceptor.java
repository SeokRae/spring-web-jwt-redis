package com.example.springwebjwtredis.aop.auth;

import com.example.springwebjwtredis.aop.exception.IllegalArgumentJwtException;
import com.example.springwebjwtredis.aop.exception.RedisNotExistException;
import com.example.springwebjwtredis.aop.jwt.JwtUtil;
import com.example.springwebjwtredis.redis.repository.CustomRedisRepository;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final CustomRedisRepository redisRepository;

    public AuthorizationInterceptor(JwtUtil jwtUtil, CustomRedisRepository redisRepository) {
        this.jwtUtil = jwtUtil;
        this.redisRepository = redisRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("AuthorizationInterceptor : preHandle()");

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header.isEmpty()) {
            throw new IllegalArgumentJwtException("인증 토큰이 존재 하지 않습니다.");
        }
        String accessToken = header.replace("Bearer ", "");

        try {
            if(jwtUtil.isValidToken(accessToken)) {
                log.info(" Valid AccessToken: {}", accessToken);
                String signature = accessToken.split("\\.")[2];

                if(hasAccessToken(signature)) { // Redis에 Token 존재 여부 확인
                    log.info(" Success Resource Access ");
                    return true;
                }
                throw new RedisNotExistException("레디스에 토큰이 존재 하지 않습니다.");
            }

        } catch(ExpiredJwtException e) {
            log.debug("Expired Jwt Exception 으로 재발급");
        }
        return false;
    }

    private boolean hasAccessToken(String hashKey) {
        return redisRepository.hasRedisByAccessToken(hashKey);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        log.info("AuthorizationInterceptor : postHandle()");
    }
}
