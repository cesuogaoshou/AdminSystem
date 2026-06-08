package com.example.admin.module.dict;

import java.time.LocalDateTime;

public record DictType(
        Long id,
        String name,
        String code,
        String description,
        Integer status,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
}
