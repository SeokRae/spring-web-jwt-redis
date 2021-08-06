package com.example.springwebjwtredis.aop.auth;

import com.example.springwebjwtredis.aop.jwt.JwtUtil;
import com.example.springwebjwtredis.domain.MemberDto;
import com.example.springwebjwtredis.dto.RequestLoginMember;
import com.example.springwebjwtredis.redis.domain.RedisMemberToken;
import com.example.springwebjwtredis.redis.repository.CustomRedisRepository;
import com.example.springwebjwtredis.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
import java.util.Map;

import static com.example.springwebjwtredis.aop.jwt.JwtUtil.TOKEN_ACCESS_EXPIRED;

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final ObjectMapper mapper = new ObjectMapper();
    private final CustomRedisRepository redisRepository;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public AuthenticationInterceptor(JwtUtil jwtUtil, MemberService memberService, CustomRedisRepository redisRepository) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
        this.redisRepository = redisRepository;
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

        request.setAttribute("memberDto", memberDto);
        return true;
    }

    private RequestLoginMember getBodyStream(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        return mapper.readValue(messageBody, RequestLoginMember.class);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("AuthenticationInterceptor : postHandle()");

        // 사용자 조회
        MemberDto memberDto = (MemberDto) request.getAttribute("memberDto");

        String tokens = jwtUtil.generateToken(memberDto, JwtUtil.TOKEN_ACCESS_EXPIRED);
        String signature = tokens.split("\\.")[2];

        redisRepository.saveRedis(
                signature,
                RedisMemberToken.builder()
                        .email(memberDto.getEmail())
                        .name(memberDto.getName())
                        .build()
        );

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokens);
    }
}
