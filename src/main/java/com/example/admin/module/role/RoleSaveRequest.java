package com.example.admin.module.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleSaveRequest(
        @NotBlank(message = "角色名称不能为空")
        @Size(max = 50, message = "角色名称长度不能超过50")
        String name,

        @NotBlank(message = "角色编码不能为空")
        @Size(max = 50, message = "角色编码长度不能超过50")
        String code,

        @Size(max = 200, message = "角色描述长度不能超过200")
        String description,

        Integer status,
        Integer sortOrder
) {
}
