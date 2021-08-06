package com.example.springwebjwtredis.redis.config;

import com.example.springwebjwtredis.redis.domain.RedisMemberToken;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    /* application-redis.yml 내의 property 파일 설정을 읽어오는 클래스 */
    private final RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    /* Redis Connector */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, RedisMemberToken> redisTemplate() {
        final RedisTemplate<String, RedisMemberToken> redisTemplate = new RedisTemplate<>();

        /* valueOperations 사용하는 경우 redis-cli 에서 keys * 조회시 키 값들의 앞에 \xac\xed\x00\x05t\x00\x04 붙는거 제거 */
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        /* valueOperations 사용하는 경우 redis-cli 에서 get 'key' 조회 시 value 값에 \xac\xed\x00\x05t\x00\x04 붙는거 제거 */
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        /* hashOperations<String, Object, Object>를 사용하는 경우 두 번째 파라미터가 HashKey */
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        /* hashOperations<String, Object, Object>를 사용하는 경우 세 번째 파라미터가 HashValue */
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RedisMemberToken.class));

        /* 트랜잭션 ?*/
        redisTemplate.setEnableTransactionSupport(true);

        /* redisTemplate은 redisConnectionFactory을 기반으로 돌아감 */
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
