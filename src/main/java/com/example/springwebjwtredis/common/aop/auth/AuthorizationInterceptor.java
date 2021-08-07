package com.example.springwebjwtredis.common.aop.auth;

import com.example.springwebjwtredis.access.domain.AccessTokenPayload;
import com.example.springwebjwtredis.access.repository.AccessTokenRepository;
import com.example.springwebjwtredis.common.aop.jwt.JwtUtil;
import com.example.springwebjwtredis.member.domain.MemberDto;
import com.example.springwebjwtredis.member.service.MemberService;
import com.example.springwebjwtredis.refresh.service.RefreshTokenService;
import io.jsonwebtoken.JwtException;
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


    /**
     * 1. accessToken의 유효성을 검사
     * 1.1
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("AuthorizationInterceptor : preHandle()");

        String accessToken = request.getHeader("jwt-access-token");
        String refreshToken = request.getHeader("jwt-refresh-token");

        try {
            // accessToken이 유효하고 Redis에도 동일한 accessToken이 존재하는 경우 자원 접근 허용
            if (jwtUtil.isValidToken(accessToken) || hasAccessToken(accessToken)) {
                return true;
            }

        } catch (JwtException e) {
            log.info("[JWT Exception] expired accessToken");
            if (jwtUtil.isValidToken(refreshToken)) {
                reGenerateAccessToken(response, refreshToken);
                return true;
            }
            log.debug("[JWT Exception] Invalid RefreshToken");
        }

        return false;
    }

    private void reGenerateAccessToken(HttpServletResponse response, String refreshToken) {
        MemberDto memberDto = jwtUtil.getMemberDto(refreshToken);

        String newAccessToken = jwtUtil.generateToken(memberDto, TOKEN_ACCESS_EXPIRED.plusTime);
        String signature = newAccessToken.split("\\.")[2];

        /* 레디스에 hashKey 값은 accessToken의 signature 값으로 설정 */
        redisRepository.saveRedis(
                signature,
                AccessTokenPayload.builder()
                        .email(memberDto.getEmail())
                        .name(memberDto.getName())
                        .build()
        );
        response.addHeader("jwt-access-token", newAccessToken);
        response.addHeader("jwt-refresh-token", refreshToken);
    }

    private boolean hasAccessToken(String hashKey) {
        return redisRepository.hasRedisByAccessToken(hashKey);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("AuthorizationInterceptor : postHandle()");
    }
}
