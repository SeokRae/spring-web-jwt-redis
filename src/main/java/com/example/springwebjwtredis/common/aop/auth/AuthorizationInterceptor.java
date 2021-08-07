package com.example.springwebjwtredis.common.aop.auth;

import com.example.springwebjwtredis.common.aop.jwt.JwtUtil;
import com.example.springwebjwtredis.member.domain.MemberDto;
import com.example.springwebjwtredis.refresh.domain.RefreshTokenEntity;
import com.example.springwebjwtredis.access.domain.AccessTokenPayload;
import com.example.springwebjwtredis.access.repository.AccessTokenRepository;
import com.example.springwebjwtredis.member.service.MemberService;
import com.example.springwebjwtredis.refresh.service.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.springwebjwtredis.common.aop.jwt.JwtExpiredType.TOKEN_ACCESS_EXPIRED;

@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final RefreshTokenService refreshTokenService;
    private final AccessTokenRepository redisRepository;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public AuthorizationInterceptor(RefreshTokenService refreshTokenService, AccessTokenRepository redisRepository, MemberService memberService, JwtUtil jwtUtil) {
        this.refreshTokenService = refreshTokenService;
        this.redisRepository = redisRepository;
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("AuthorizationInterceptor : preHandle()");

        String accessToken = request.getHeader("jwt-access-token");
        String refreshToken = request.getHeader("jwt-refresh-token");

        try {

            if (jwtUtil.isValidToken(accessToken)) { // 1. accessToken의 유효성 검사
                log.info(" Valid AccessToken: {}", accessToken);
                String signature = accessToken.split("\\.")[2];

                if (!hasAccessToken(signature)) { // 2. Redis에 accessToken 보유 여부
                    log.info(" Not Exists AccessToken in Redis ");
                } else {
                    log.info(" Success Resource Access ");
                    return true;
                }
            }

        } catch (ExpiredJwtException e) {
            log.debug("Expired Jwt Exception 으로 재발급");
            if (jwtUtil.isValidToken(refreshToken)) {
                RefreshTokenEntity byEmail = refreshTokenService.findByEmail(jwtUtil.getMemberByEmail(refreshToken));


            }
        }
        return false;
    }

    private String generateAccessToken(MemberDto memberDto) {
        String tokens = jwtUtil.generateToken(memberDto, TOKEN_ACCESS_EXPIRED.plusTime);
        String signature = tokens.split("\\.")[2];

        redisRepository.saveRedis(
                signature,
                AccessTokenPayload.builder()
                        .email(memberDto.getEmail())
                        .name(memberDto.getName())
                        .build()
        );
        return tokens;
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
