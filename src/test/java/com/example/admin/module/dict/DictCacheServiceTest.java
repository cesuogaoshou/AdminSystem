package com.example.admin.module.dict;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DictCacheServiceTest {

    @Test
    void getItemsShouldReturnCachedDictItemsWhenTypeCodeExists() {
        RedisTemplate<String, Object> redisTemplate = redisTemplate();
        ValueOperations<String, Object> valueOperations = valueOperations();
        List<DictItem> items = List.of(dictItem());
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("admin:dict:items:gender")).thenReturn(items);
        DictCacheService dictCacheService = new DictCacheService(redisTemplate);

        List<DictItem> result = dictCacheService.getItems("gender");

        assertThat(result).containsExactly(dictItem());
    }

    @Test
    void getItemsShouldReturnNullWhenCachedValueIsNotList() {
        RedisTemplate<String, Object> redisTemplate = redisTemplate();
        ValueOperations<String, Object> valueOperations = valueOperations();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("admin:dict:items:gender")).thenReturn("bad-cache");
        DictCacheService dictCacheService = new DictCacheService(redisTemplate);

        assertThat(dictCacheService.getItems("gender")).isNull();
    }

    @Test
    void putItemsShouldStoreDictItems() {
        RedisTemplate<String, Object> redisTemplate = redisTemplate();
        ValueOperations<String, Object> valueOperations = valueOperations();
        List<DictItem> items = List.of(dictItem());
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        DictCacheService dictCacheService = new DictCacheService(redisTemplate);

        dictCacheService.putItems("gender", items);

        verify(valueOperations).set("admin:dict:items:gender", items);
    }

    @Test
    void evictItemsShouldDeleteDictCacheKey() {
        RedisTemplate<String, Object> redisTemplate = redisTemplate();
        DictCacheService dictCacheService = new DictCacheService(redisTemplate);

        dictCacheService.evictItems("gender");

        verify(redisTemplate).delete("admin:dict:items:gender");
    }

    @SuppressWarnings("unchecked")
    private RedisTemplate<String, Object> redisTemplate() {
        return mock(RedisTemplate.class);
    }

    @SuppressWarnings("unchecked")
    private ValueOperations<String, Object> valueOperations() {
        return mock(ValueOperations.class);
    }

    private DictItem dictItem() {
        LocalDateTime now = LocalDateTime.of(2026, 6, 8, 19, 0);
        return new DictItem(1L, 1L, "男", "1", "blue", 1, 1, now, now);
    }
}
