package com.example.springwebjwtredis.access.exception;

public class NotFoundAccessTokenException extends RuntimeException {
    public NotFoundAccessTokenException() {
    }

    public NotFoundAccessTokenException(String message) {
        super(message);
    }
}
