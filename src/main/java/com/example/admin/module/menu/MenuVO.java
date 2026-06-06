package com.example.admin.module.menu;

import java.util.List;

public record MenuVO(
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
        List<MenuVO> children
) {
}
