package com.example.springwebjwtredis.aop;

import org.springframework.http.ResponseEntity;

public class GlobalExceptionHandler {

    //    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> defaultExceptionHandler(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
