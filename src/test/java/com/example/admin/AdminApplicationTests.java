package com.example.admin;

import com.example.admin.module.dept.DeptMapper;
import com.example.admin.module.log.LogMapper;
import com.example.admin.module.menu.MenuMapper;
import com.example.admin.module.role.RoleMapper;
import com.example.admin.module.user.UserMapper;
import com.example.admin.security.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
        "admin.mybatis.enabled=false",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration"
})
class AdminApplicationTests {

    @MockitoBean
    private DeptMapper deptMapper;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private RoleMapper roleMapper;

    @MockitoBean
    private MenuMapper menuMapper;

    @MockitoBean
    private LogMapper logMapper;

    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;

    @Test
    void contextLoads() {
    }
}
