package com.example.springwebjwtredis.member.exception;

public class MemberNotFoundException extends IllegalStateException {
    public MemberNotFoundException() {
    }

    public MemberNotFoundException(String msg) {
        super(msg);
    }
}
