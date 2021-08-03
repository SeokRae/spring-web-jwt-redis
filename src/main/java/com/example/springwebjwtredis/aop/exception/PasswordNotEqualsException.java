package com.example.springwebjwtredis.aop.exception;

public class PasswordNotEqualsException extends IllegalArgumentException {
    public PasswordNotEqualsException() {
    }

    public PasswordNotEqualsException(String msg) {
        super(msg);
    }
}
