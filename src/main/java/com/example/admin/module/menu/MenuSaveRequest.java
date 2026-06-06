package com.example.admin.module.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MenuSaveRequest(
        Long parentId,

        @NotBlank(message = "菜单名称不能为空")
        @Size(max = 50, message = "菜单名称长度不能超过50")
        String name,

        @NotNull(message = "菜单类型不能为空")
        Integer type,

        @Size(max = 200, message = "路由路径长度不能超过200")
        String path,

        @Size(max = 200, message = "组件路径长度不能超过200")
        String component,

        @Size(max = 200, message = "权限标识长度不能超过200")
        String permission,

        @Size(max = 100, message = "图标长度不能超过100")
        String icon,

        Integer sortOrder,
        Integer visible
) {
}
