package com.example.admin.module.dept;

import java.time.LocalDateTime;

public record Dept(
        Long id,
        Long parentId,
        String name,
        String leader,
        String phone,
        Integer sortOrder,
        Integer status,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
}