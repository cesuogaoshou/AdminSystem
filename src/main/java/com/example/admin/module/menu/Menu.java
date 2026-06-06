package com.example.admin.module.menu;

import java.time.LocalDateTime;

public record Menu(
        Long id,
        Long parentId,
        String name,
        Integer type,
        String path,
        String component,
        String permission,
        String icon,
        Integer sortOrder,
        Integer visible,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
}
