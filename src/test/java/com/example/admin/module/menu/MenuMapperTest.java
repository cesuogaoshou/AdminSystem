package com.example.admin.module.menu;

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
@MapperScan("com.example.admin.module.menu")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/admin_system?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
        "spring.datasource.username=root",
        "spring.datasource.password=${ADMIN_DB_PASSWORD}",
        "spring.flyway.enabled=false"
})
class MenuMapperTest {

    @Autowired
    private MenuMapper menuMapper;

    @Test
    void findAllShouldReturnInitialMenus() {
        List<Menu> menus = menuMapper.findAll();

        assertThat(menus).isNotEmpty();
        assertThat(menus)
                .extracting(Menu::permission)
                .contains(
                        "sys:user:list",
                        "sys:role:list",
                        "sys:menu:list",
                        "sys:dept:list",
                        "sys:dict:list",
                        "sys:log:list"
                );
    }

    @Test
    void findByIdShouldReturnMenu() {
        Menu menu = menuMapper.findById(1L);

        assertThat(menu).isNotNull();
        assertThat(menu.parentId()).isZero();
        assertThat(menu.type()).isEqualTo(1);
    }

    @Test
    void countByParentIdShouldReturnChildrenCount() {
        int count = menuMapper.countByParentId(1L);

        assertThat(count).isEqualTo(6);
    }

    @Test
    void countRolesByMenuIdShouldReturnAssignedRoleCount() {
        int count = menuMapper.countRolesByMenuId(1L);

        assertThat(count).isEqualTo(1);
    }
}
