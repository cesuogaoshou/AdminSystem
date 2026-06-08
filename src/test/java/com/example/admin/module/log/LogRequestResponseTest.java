package com.example.admin.module.log;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LogRequestResponseTest {

    @Test
    void logQueryRequestShouldProvideDefaultPageAndSize() {
        LogQueryRequest query = new LogQueryRequest(null, null, null, null, null, null, null);

        assertThat(query.pageOrDefault()).isEqualTo(1);
        assertThat(query.sizeOrDefault()).isEqualTo(10);
    }

    @Test
    void logQueryRequestShouldKeepFilterFields() {
        LocalDateTime startTime = LocalDateTime.of(2026, 6, 8, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 6, 8, 12, 0);
        LogQueryRequest query = new LogQueryRequest(
                2,
                20,
                "admin",
                "用户管理",
                1,
                startTime,
                endTime
        );

        assertThat(query.pageOrDefault()).isEqualTo(2);
        assertThat(query.sizeOrDefault()).isEqualTo(20);
        assertThat(query.username()).isEqualTo("admin");
        assertThat(query.module()).isEqualTo("用户管理");
        assertThat(query.status()).isOne();
        assertThat(query.startTime()).isEqualTo(startTime);
        assertThat(query.endTime()).isEqualTo(endTime);
    }

    @Test
    void logVOShouldHoldListFields() {
        LocalDateTime createTime = LocalDateTime.of(2026, 6, 8, 10, 30);
        LogVO logVO = new LogVO(
                1L,
                "admin",
                "用户管理",
                "新增用户",
                "POST",
                "/api/users",
                "127.0.0.1",
                36L,
                1,
                createTime
        );

        assertThat(logVO.id()).isEqualTo(1L);
        assertThat(logVO.username()).isEqualTo("admin");
        assertThat(logVO.module()).isEqualTo("用户管理");
        assertThat(logVO.operation()).isEqualTo("新增用户");
        assertThat(logVO.method()).isEqualTo("POST");
        assertThat(logVO.requestUrl()).isEqualTo("/api/users");
        assertThat(logVO.requestIp()).isEqualTo("127.0.0.1");
        assertThat(logVO.duration()).isEqualTo(36L);
        assertThat(logVO.status()).isOne();
        assertThat(logVO.createTime()).isEqualTo(createTime);
    }
}
