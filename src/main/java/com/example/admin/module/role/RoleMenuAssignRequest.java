package com.example.admin.module.role;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoleMenuAssignRequest(
        @NotNull(message = "菜单ID列表不能为空")
        List<Long> menuIds
) {
}
