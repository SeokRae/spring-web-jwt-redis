package com.example.springwebjwtredis.access.repository;

import com.example.springwebjwtredis.access.domain.AccessTokenPayload;
import com.example.springwebjwtredis.access.exception.NotFoundAccessTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class AccessTokenRepository {

    private final HashOperations<String, String, AccessTokenPayload> hashOperations;

    public AccessTokenRepository(RedisTemplate<String, AccessTokenPayload> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void saveRedis(String signature, AccessTokenPayload redisToken) {
        log.info("saveRedis : {}, {}", signature, redisToken);
        hashOperations.put("member", signature, redisToken);
    }

    public AccessTokenPayload findRedisBySignature(String signature) {
        AccessTokenPayload member = hashOperations.get("member", signature);
        return Optional.ofNullable(member)
                .orElseThrow(() -> new NotFoundAccessTokenException("Redis Not Found Exception "));
    }

    public boolean hasRedisByAccessToken(String signature) {
        return hashOperations.hasKey("member", signature);
    }

}
