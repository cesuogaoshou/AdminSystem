package com.example.admin;

import com.example.admin.module.dept.DeptMapper;
import com.example.admin.module.dict.DictCacheService;
import com.example.admin.module.dict.DictItemMapper;
import com.example.admin.module.dict.DictTypeMapper;
import com.example.admin.module.log.LogMapper;
import com.example.admin.module.log.LogPublisher;
import com.example.admin.module.menu.MenuMapper;
import com.example.admin.module.role.RoleMapper;
import com.example.admin.module.user.UserMapper;
import com.example.admin.security.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "admin.mybatis.enabled=false",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration"
})
@AutoConfigureMockMvc
class AdminApplicationTests {

    @Autowired
    private MockMvc mockMvc;

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
    private LogPublisher logPublisher;

    @MockitoBean
    private DictTypeMapper dictTypeMapper;

    @MockitoBean
    private DictItemMapper dictItemMapper;

    @MockitoBean
    private DictCacheService dictCacheService;

    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;

    @MockitoBean
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    void contextLoads() {
    }

    @Test
    void apiDocsShouldReturnOpenApiDefinition() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").exists());
    }
}
