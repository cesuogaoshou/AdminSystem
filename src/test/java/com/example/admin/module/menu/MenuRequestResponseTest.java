package com.example.admin.module.menu;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuRequestResponseTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void menuShouldHoldAllFields() {
        LocalDateTime now = LocalDateTime.now();

        Menu menu = new Menu(
                1L, 0L, "系统管理", 1, "/system",
                null, null, "setting", 1, 1, now, now
        );

        assertThat(menu.id()).isEqualTo(1L);
        assertThat(menu.parentId()).isZero();
        assertThat(menu.name()).isEqualTo("系统管理");
        assertThat(menu.type()).isEqualTo(1);
        assertThat(menu.path()).isEqualTo("/system");
        assertThat(menu.icon()).isEqualTo("setting");
        assertThat(menu.sortOrder()).isEqualTo(1);
        assertThat(menu.visible()).isEqualTo(1);
        assertThat(menu.createTime()).isEqualTo(now);
        assertThat(menu.updateTime()).isEqualTo(now);
    }

    @Test
    void saveRequestShouldRejectBlankName() {
        MenuSaveRequest request = new MenuSaveRequest(
                0L, "", 1, "/system", null,
                null, "setting", 1, 1
        );

        assertThat(validator.validate(request))
                .anyMatch(violation -> violation.getMessage().equals("菜单名称不能为空"));
    }

    @Test
    void saveRequestShouldRejectNullType() {
        MenuSaveRequest request = new MenuSaveRequest(
                0L, "系统管理", null, "/system", null,
                null, "setting", 1, 1
        );

        assertThat(validator.validate(request))
                .anyMatch(violation -> violation.getMessage().equals("菜单类型不能为空"));
    }

    @Test
    void menuVoShouldHoldTreeNodeFields() {
        MenuVO child = new MenuVO(
                2L, 1L, "用户管理", 2, "/system/users",
                "system/user/index", "sys:user:list", "user",
                1, 1, List.of()
        );
        MenuVO root = new MenuVO(
                1L, 0L, "系统管理", 1, "/system",
                null, null, "setting", 1, 1, List.of(child)
        );

        assertThat(root.name()).isEqualTo("系统管理");
        assertThat(root.children()).containsExactly(child);
        assertThat(root.children().getFirst().permission()).isEqualTo("sys:user:list");
    }
}
