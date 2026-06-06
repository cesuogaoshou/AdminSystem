package com.example.admin.module.dept;

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
@MapperScan("com.example.admin.module.dept")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/admin_system?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
        "spring.datasource.username=root",
        "spring.datasource.password=${ADMIN_DB_PASSWORD}",
        "spring.flyway.enabled=false"
})
class DeptMapperTest {

    @Autowired
    private DeptMapper deptMapper;

    @Test
    void findAllShouldReturnInitialDepartments() {
        List<Dept> depts = deptMapper.findAll();

        assertThat(depts).isNotEmpty();
        assertThat(depts)
                .extracting(Dept::name)
                .contains("总公司", "技术部", "产品部");
    }

    @Test
    void findByIdShouldReturnDept() {
        Dept dept = deptMapper.findById(2L);

        assertThat(dept).isNotNull();
        assertThat(dept.name()).isEqualTo("技术部");
        assertThat(dept.parentId()).isEqualTo(1L);
    }
}
