package com.example.springwebjwtredis.redis.repository;

import com.example.springwebjwtredis.aop.jwt.JwtUtil;
import com.example.springwebjwtredis.redis.domain.RedisMemberToken;
import com.example.springwebjwtredis.redis.exception.RedisNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class CustomRedisRepository {

    private final HashOperations<String, String, RedisMemberToken> hashOperations;

    public CustomRedisRepository(RedisTemplate<String, RedisMemberToken> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void saveRedis(String signature, RedisMemberToken redisToken) {
        log.info("saveRedis : {}, {}", signature, redisToken);
        hashOperations.put("member", signature, redisToken);
    }

    public RedisMemberToken findRedisBySignature(String signature) {
        RedisMemberToken member = hashOperations.get("member", signature);
        return Optional.ofNullable(member)
                .orElseThrow(() -> new RedisNotFoundException("Redis Not Found Exception "));
    }

    public boolean hasRedisByAccessToken(String accessToken) {
        return hashOperations.hasKey("member", accessToken);
    }

}
