package com.example.admin.module.user;

import java.time.LocalDateTime;

public record User(
        Long id,
        String username,
        String password,
        String nickname,
        String email,
        String phone,
        Integer gender,
        String avatar,
        Long deptId,
        Integer status,
        String createBy,
        LocalDateTime createTime,
        String updateBy,
        LocalDateTime updateTime,
        Integer deleted
) {
}