package com.example.admin.module.user;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserRoleAssignRequest(
        @NotNull(message = "角色ID列表不能为空")
        List<Long> roleIds
) {
}
