package com.example.admin.module.dict;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictCacheService {

    private static final String KEY_PREFIX = "admin:dict:items:";

    private final RedisTemplate<String, Object> redisTemplate;

    public DictCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public List<DictItem> getItems(String typeCode) {
        Object cachedValue = redisTemplate.opsForValue().get(key(typeCode));
        if (!(cachedValue instanceof List<?> items)) {
            return null;
        }

        return items.stream()
                .filter(DictItem.class::isInstance)
                .map(DictItem.class::cast)
                .toList();
    }

    public void putItems(String typeCode, List<DictItem> items) {
        redisTemplate.opsForValue().set(key(typeCode), items);
    }

    public void evictItems(String typeCode) {
        redisTemplate.delete(key(typeCode));
    }

    private String key(String typeCode) {
        return KEY_PREFIX + typeCode;
    }
}
