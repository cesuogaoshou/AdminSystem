package com.example.admin.module.user;

import java.time.LocalDateTime;

public record UserVO(
        Long id,
        String username,
        String nickname,
        String email,
        String phone,
        Integer gender,
        String avatar,
        Long deptId,
        String deptName,
        Integer status,
        LocalDateTime createTime
) {
}