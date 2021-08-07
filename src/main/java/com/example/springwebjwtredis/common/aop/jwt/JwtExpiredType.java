package com.example.springwebjwtredis.common.aop.jwt;

public enum JwtExpiredType {
    TOKEN_DEFAULT_EXPIRED(0),
    TOKEN_ACCESS_EXPIRED(1),
    TOKEN_REFRESH_EXPIRED(5);

    public final int plusTime;

    JwtExpiredType(int plusTime) {
        this.plusTime = plusTime;
    }
}
