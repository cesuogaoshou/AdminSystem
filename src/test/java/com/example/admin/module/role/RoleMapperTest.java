package com.example.admin.module.role;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MapperScan("com.example.admin.module.role")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/admin_system?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
        "spring.datasource.username=root",
        "spring.datasource.password=${ADMIN_DB_PASSWORD}",
        "spring.flyway.enabled=false"
})
class RoleMapperTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    void findByIdShouldReturnAdminRole() {
        Role role = roleMapper.findById(1L);

        assertThat(role).isNotNull();
        assertThat(role.code()).isEqualTo("admin");
        assertThat(role.status()).isEqualTo(1);
    }

    @Test
    void findByCodeShouldReturnAdminRole() {
        Role role = roleMapper.findByCode("admin");

        assertThat(role).isNotNull();
        assertThat(role.id()).isEqualTo(1L);
        assertThat(role.sortOrder()).isEqualTo(1);
    }

    @Test
    void countByCodeShouldReturnOneForAdmin() {
        int count = roleMapper.countByCode("admin");

        assertThat(count).isEqualTo(1);
    }

    @Test
    void findPageShouldFilterByCodeNameAndStatus() {
        RoleQueryRequest query = new RoleQueryRequest(1, 10, "admin", 1);

        List<RoleVO> roles = roleMapper.findPage(query);

        assertThat(roles).hasSize(1);
        assertThat(roles.getFirst().code()).isEqualTo("admin");
        assertThat(roles.getFirst().status()).isEqualTo(1);
    }
}
