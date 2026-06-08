package com.example.admin.module.log;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SysLogTest {

    @Test
    void sysLogShouldHoldOperationLogFields() {
        LocalDateTime createTime = LocalDateTime.of(2026, 6, 8, 10, 30);

        SysLog sysLog = new SysLog(
                1L,
                "admin",
                "用户管理",
                "新增用户",
                "POST",
                "/api/users",
                "127.0.0.1",
                "{\"username\":\"test\"}",
                "{\"code\":200}",
                36L,
                1,
                null,
                createTime
        );

        assertThat(sysLog.id()).isEqualTo(1L);
        assertThat(sysLog.username()).isEqualTo("admin");
        assertThat(sysLog.module()).isEqualTo("用户管理");
        assertThat(sysLog.operation()).isEqualTo("新增用户");
        assertThat(sysLog.method()).isEqualTo("POST");
        assertThat(sysLog.requestUrl()).isEqualTo("/api/users");
        assertThat(sysLog.requestIp()).isEqualTo("127.0.0.1");
        assertThat(sysLog.params()).contains("username");
        assertThat(sysLog.result()).contains("code");
        assertThat(sysLog.duration()).isEqualTo(36L);
        assertThat(sysLog.status()).isOne();
        assertThat(sysLog.errorMsg()).isNull();
        assertThat(sysLog.createTime()).isEqualTo(createTime);
    }
}
