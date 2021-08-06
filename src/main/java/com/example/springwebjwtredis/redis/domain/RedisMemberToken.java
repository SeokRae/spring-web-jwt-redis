package com.example.springwebjwtredis.redis.domain;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@ToString
@RedisHash("user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisMemberToken implements Serializable {

    private String email;
    private String name;

    @Builder
    public RedisMemberToken(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
