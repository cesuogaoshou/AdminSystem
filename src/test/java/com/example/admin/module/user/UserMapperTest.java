package com.example.admin.module.user;

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
@MapperScan("com.example.admin.module.user")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/admin_system?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
        "spring.datasource.username=root",
        "spring.datasource.password=lilizj2580",
        "spring.flyway.enabled=false"
})
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void findByIdShouldReturnAdminUser() {
        User user = userMapper.findById(1L);

        assertThat(user).isNotNull();
        assertThat(user.username()).isEqualTo("admin");
        assertThat(user.deleted()).isZero();
    }

    @Test
    void findByUsernameShouldReturnAdminUser() {
        User user = userMapper.findByUsername("admin");

        assertThat(user).isNotNull();
        assertThat(user.id()).isEqualTo(1L);
        assertThat(user.status()).isEqualTo(1);
    }

    @Test
    void countByUsernameShouldReturnOneForAdmin() {
        int count = userMapper.countByUsername("admin");

        assertThat(count).isEqualTo(1);
    }

    @Test
    void findPageShouldReturnUserVoWithDeptName() {
        UserQueryRequest query = new UserQueryRequest(1, 10, "admin", 1, 2L);

        List<UserVO> users = userMapper.findPage(query);

        assertThat(users).hasSize(1);
        assertThat(users.getFirst().username()).isEqualTo("admin");
        assertThat(users.getFirst().deptName()).isEqualTo("技术部");
    }
}