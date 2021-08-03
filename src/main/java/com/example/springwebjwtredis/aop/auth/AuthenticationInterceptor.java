package com.example.springwebjwtredis.aop.auth;

import com.example.springwebjwtredis.aop.jwt.JwtUtil;
import com.example.springwebjwtredis.domain.MemberDto;
import com.example.springwebjwtredis.dto.RequestLoginMember;
import com.example.springwebjwtredis.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MemberService memberService;

    public AuthenticationInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private RequestLoginMember getBodyStream(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        return mapper.readValue(messageBody, RequestLoginMember.class);
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

        String tokens = jwtUtil.createTokens(memberDto);

        response.addHeader("Authorization", "Bearer " + tokens);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("AuthenticationInterceptor : postHandle()");
    }
}
