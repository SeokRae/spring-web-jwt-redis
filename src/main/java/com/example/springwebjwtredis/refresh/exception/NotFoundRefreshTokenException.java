package com.example.springwebjwtredis.refresh.exception;

public class NotFoundRefreshTokenException extends RuntimeException {
    public NotFoundRefreshTokenException(String message) {
        super(message);
    }
}
