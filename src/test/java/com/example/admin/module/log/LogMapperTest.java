package com.example.admin.module.log;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MapperScan("com.example.admin.module.log")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/admin_system?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
        "spring.datasource.username=root",
        "spring.datasource.password=${ADMIN_DB_PASSWORD}",
        "spring.flyway.enabled=false"
})
class LogMapperTest {

    @Autowired
    private LogMapper logMapper;

    @Test
    void insertShouldCreateLogAndFindByIdShouldReturnIt() {
        SysLog sysLog = new SysLog(
                900000000001L,
                "admin",
                "用户管理",
                "新增用户",
                "POST",
                "/api/users",
                "127.0.0.1",
                "{\"username\":\"test\"}",
                "{\"code\":200}",
                42L,
                1,
                null,
                null
        );

        int rows = logMapper.insert(sysLog);

        assertThat(rows).isEqualTo(1);

        SysLog savedLog = logMapper.findById(sysLog.id());
        assertThat(savedLog).isNotNull();
        assertThat(savedLog.username()).isEqualTo("admin");
        assertThat(savedLog.module()).isEqualTo("用户管理");
        assertThat(savedLog.operation()).isEqualTo("新增用户");
        assertThat(savedLog.method()).isEqualTo("POST");
        assertThat(savedLog.requestUrl()).isEqualTo("/api/users");
        assertThat(savedLog.requestIp()).isEqualTo("127.0.0.1");
        assertThat(savedLog.params()).contains("username");
        assertThat(savedLog.result()).contains("code");
        assertThat(savedLog.duration()).isEqualTo(42L);
        assertThat(savedLog.status()).isOne();
        assertThat(savedLog.errorMsg()).isNull();
        assertThat(savedLog.createTime()).isNotNull();
    }
}
