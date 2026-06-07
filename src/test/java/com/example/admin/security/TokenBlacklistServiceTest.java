package com.example.admin.security;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TokenBlacklistServiceTest {

    @Test
    void blacklistShouldStoreTokenWithTtl() {
        RedisTemplate<String, Object> redisTemplate = redisTemplate();
        ValueOperations<String, Object> valueOperations = valueOperations();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        TokenBlacklistService tokenBlacklistService = new TokenBlacklistService(redisTemplate);

        tokenBlacklistService.blacklist("token-value", 3600L);

        verify(valueOperations).set(
                "admin:token:blacklist:token-value",
                "1",
                3600L,
                TimeUnit.MILLISECONDS
        );
    }

    @Test
    void isBlacklistedShouldReturnTrueWhenRedisKeyExists() {
        RedisTemplate<String, Object> redisTemplate = redisTemplate();
        when(redisTemplate.hasKey("admin:token:blacklist:token-value")).thenReturn(true);
        TokenBlacklistService tokenBlacklistService = new TokenBlacklistService(redisTemplate);

        assertThat(tokenBlacklistService.isBlacklisted("token-value")).isTrue();
    }

    @Test
    void isBlacklistedShouldReturnFalseWhenRedisKeyMissing() {
        RedisTemplate<String, Object> redisTemplate = redisTemplate();
        when(redisTemplate.hasKey("admin:token:blacklist:token-value")).thenReturn(false);
        TokenBlacklistService tokenBlacklistService = new TokenBlacklistService(redisTemplate);

        assertThat(tokenBlacklistService.isBlacklisted("token-value")).isFalse();
    }

    @SuppressWarnings("unchecked")
    private RedisTemplate<String, Object> redisTemplate() {
        return mock(RedisTemplate.class);
    }

    @SuppressWarnings("unchecked")
    private ValueOperations<String, Object> valueOperations() {
        return mock(ValueOperations.class);
    }
}
