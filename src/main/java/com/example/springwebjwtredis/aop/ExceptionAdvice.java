package com.example.springwebjwtredis.aop;

import com.example.springwebjwtredis.aop.exception.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MemberNotFoundException.class)
    protected ResponseEntity<String> memberNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보가 존재하지 않습니다.");
    }
}
