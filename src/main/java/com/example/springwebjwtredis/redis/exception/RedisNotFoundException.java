package com.example.springwebjwtredis.redis.exception;

public class RedisNotFoundException extends RuntimeException {
    public RedisNotFoundException() {
    }

    public RedisNotFoundException(String message) {
        super(message);
    }
}
