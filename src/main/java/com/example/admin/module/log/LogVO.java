package com.example.admin.module.log;

import java.time.LocalDateTime;

public record LogVO(
        Long id,
        String username,
        String module,
        String operation,
        String method,
        String requestUrl,
        String requestIp,
        Long duration,
        Integer status,
        LocalDateTime createTime
) {
}
