package com.example.springwebjwtredis.aop.exception;

public class IllegalArgumentJwtException extends RuntimeException {
    public IllegalArgumentJwtException() {
    }

    public IllegalArgumentJwtException(String message) {
        super(message);
    }
}
