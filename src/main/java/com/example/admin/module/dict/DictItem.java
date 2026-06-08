package com.example.admin.module.dict;

import java.time.LocalDateTime;

public record DictItem(
        Long id,
        Long typeId,
        String label,
        String value,
        String color,
        Integer sortOrder,
        Integer status,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
}
