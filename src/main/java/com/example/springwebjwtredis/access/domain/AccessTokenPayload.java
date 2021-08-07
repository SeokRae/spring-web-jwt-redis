package com.example.springwebjwtredis.access.domain;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@ToString
@RedisHash("member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessTokenPayload implements Serializable {

    private String email;
    private String name;

    @Builder
    public AccessTokenPayload(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
