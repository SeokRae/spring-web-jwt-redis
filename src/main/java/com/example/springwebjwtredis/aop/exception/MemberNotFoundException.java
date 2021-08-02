package com.example.springwebjwtredis.aop.exception;

public class MemberNotFoundException extends IllegalStateException {
    public MemberNotFoundException() {
    }

    public MemberNotFoundException(String msg) {
        super(msg);
    }
}
