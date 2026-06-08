package com.example.admin.module.log;

import java.time.LocalDateTime;

public record SysLog(
        Long id,
        String username,
        String module,
        String operation,
        String method,
        String requestUrl,
        String requestIp,
        String params,
        String result,
        Long duration,
        Integer status,
        String errorMsg,
        LocalDateTime createTime
) {
}
