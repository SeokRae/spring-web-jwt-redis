package com.example.springwebjwtredis.aop.exception;

public class RedisNotExistException extends RuntimeException {
    public RedisNotExistException() {
    }

    public RedisNotExistException(String message) {
        super(message);
    }
}
