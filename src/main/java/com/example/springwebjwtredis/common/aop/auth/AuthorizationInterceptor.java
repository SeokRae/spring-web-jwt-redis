package com.example.springwebjwtredis.common.aop.auth;

import com.example.springwebjwtredis.access.domain.AccessTokenPayload;
import com.example.springwebjwtredis.access.repository.AccessTokenRepository;
import com.example.springwebjwtredis.common.aop.jwt.JwtUtil;
import com.example.springwebjwtredis.member.domain.MemberDto;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.springwebjwtredis.common.aop.jwt.JwtExpiredType.TOKEN_ACCESS_EXPIRED;

@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final AccessTokenRepository accessTokenRepository;
    private final JwtUtil jwtUtil;

    public AuthorizationInterceptor(AccessTokenRepository accessTokenRepository, JwtUtil jwtUtil) {
        this.accessTokenRepository = accessTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("AuthorizationInterceptor : preHandle()");

        String accessToken = request.getHeader("jwt-access-token");
        String refreshToken = request.getHeader("jwt-refresh-token");

        try {
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
        accessTokenRepository.saveRedis(
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
        return accessTokenRepository.hasRedisByAccessToken(hashKey);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("AuthorizationInterceptor : postHandle()");
    }
}
