package com.example.admin.module.role;

import java.time.LocalDateTime;

public record RoleVO(
        Long id,
        String name,
        String code,
        String description,
        Integer status,
        Integer sortOrder,
        LocalDateTime createTime
) {
}
