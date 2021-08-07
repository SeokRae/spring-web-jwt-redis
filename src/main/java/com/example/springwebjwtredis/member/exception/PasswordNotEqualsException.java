package com.example.springwebjwtredis.member.exception;

public class PasswordNotEqualsException extends IllegalArgumentException {
    public PasswordNotEqualsException() {
    }

    public PasswordNotEqualsException(String msg) {
        super(msg);
    }
}
