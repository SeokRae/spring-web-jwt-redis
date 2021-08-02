package com.example.springwebjwtredis.redis.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisDto {
    private String username;
    private String role;

    @Builder
    public RedisDto(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
