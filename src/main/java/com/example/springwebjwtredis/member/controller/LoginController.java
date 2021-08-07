package com.example.springwebjwtredis.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
public class LoginController {

    @PostMapping(value = "/login")
    public void login() {
        log.info("사용자 로그인");
    }
}
