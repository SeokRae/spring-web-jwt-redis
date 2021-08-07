package com.example.springwebjwtredis.common.aop.auth;

import com.example.springwebjwtredis.access.domain.AccessTokenPayload;
import com.example.springwebjwtredis.access.repository.AccessTokenRepository;
import com.example.springwebjwtredis.common.aop.jwt.JwtUtil;
import com.example.springwebjwtredis.member.domain.MemberDto;
import com.example.springwebjwtredis.member.dto.RequestLoginMember;
import com.example.springwebjwtredis.member.service.MemberService;
import com.example.springwebjwtredis.refresh.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.StreamUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.example.springwebjwtredis.common.aop.jwt.JwtExpiredType.TOKEN_DEFAULT_EXPIRED;
import static com.example.springwebjwtredis.common.aop.jwt.JwtExpiredType.TOKEN_REFRESH_EXPIRED;

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final ObjectMapper mapper = new ObjectMapper();
    private final AccessTokenRepository redisRepository;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

    private final JwtUtil jwtUtil;

    public AuthenticationInterceptor(JwtUtil jwtUtil, MemberService memberService, AccessTokenRepository redisRepository, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
        this.redisRepository = redisRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("AuthenticationInterceptor : preHandle()");

        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            throw new HttpRequestMethodNotSupportedException("해당 요청을 제공하지 않습니다.");
        }

        RequestLoginMember loginMember = getBodyStream(request);
        MemberDto memberDto = memberService.findMember(loginMember.getEmail(), loginMember.getPassword());

        if (memberDto == null) {
            return false;
        }

        // accessToken, refreshToken 발급
        String accessToken = generateAccessToken(memberDto);
        String refreshToken = generateRefreshToken(memberDto);

        response.addHeader("jwt-access-token", accessToken);
        response.addHeader("jwt-refresh-token", refreshToken);
        return true;
    }

    private RequestLoginMember getBodyStream(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        return mapper.readValue(messageBody, RequestLoginMember.class);
    }

    /**
     * 엑세스 토큰 발급
     */
    private String generateAccessToken(MemberDto memberDto) {
        String tokens = jwtUtil.generateToken(memberDto, TOKEN_DEFAULT_EXPIRED.plusTime);
        String signature = tokens.split("\\.")[2];

        /* 레디스에 hashKey 값은 accessToken의 signature 값으로 설정 */
        redisRepository.saveRedis(
                signature,
                AccessTokenPayload.builder()
                        .email(memberDto.getEmail())
                        .name(memberDto.getName())
                        .build()
        );
        return tokens;
    }

    /**
     * 리플레시 토큰 발급
     */
    private String generateRefreshToken(MemberDto memberDto) {
        String refreshToken = jwtUtil.generateToken(memberDto, TOKEN_REFRESH_EXPIRED.plusTime);
        refreshTokenService.save(memberDto, refreshToken);
        return refreshToken;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("AuthenticationInterceptor : postHandle()");
    }
}
