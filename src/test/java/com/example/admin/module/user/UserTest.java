package com.example.admin.module.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void userShouldKeepAllFields() {
        LocalDateTime now = LocalDateTime.now();

        User user = new User(
                1L,
                "admin",
                "encoded-password",
                "超级管理员",
                "admin@example.com",
                "10000000000",
                0,
                "https://example.com/avatar.png",
                2L,
                1,
                "system",
                now,
                "system",
                now,
                0
        );

        assertThat(user.id()).isEqualTo(1L);
        assertThat(user.username()).isEqualTo("admin");
        assertThat(user.password()).isEqualTo("encoded-password");
        assertThat(user.nickname()).isEqualTo("超级管理员");
        assertThat(user.email()).isEqualTo("admin@example.com");
        assertThat(user.phone()).isEqualTo("10000000000");
        assertThat(user.gender()).isZero();
        assertThat(user.avatar()).isEqualTo("https://example.com/avatar.png");
        assertThat(user.deptId()).isEqualTo(2L);
        assertThat(user.status()).isEqualTo(1);
        assertThat(user.createBy()).isEqualTo("system");
        assertThat(user.createTime()).isEqualTo(now);
        assertThat(user.updateBy()).isEqualTo("system");
        assertThat(user.updateTime()).isEqualTo(now);
        assertThat(user.deleted()).isZero();
    }
}