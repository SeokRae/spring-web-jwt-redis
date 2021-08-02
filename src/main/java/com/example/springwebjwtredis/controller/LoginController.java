package com.example.springwebjwtredis.controller;

import com.example.springwebjwtredis.dto.RequestLoginMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @PostMapping(value = "/login")
    public void login(@RequestBody RequestLoginMember loginMember) {
        log.info("사용자 로그인: {}", loginMember);
    }
}
