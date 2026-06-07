package com.example.admin.module.role;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoleRequestResponseTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void roleShouldHoldAllFields() {
        LocalDateTime now = LocalDateTime.now();

        Role role = new Role(
                1L, "超级管理员", "admin", "拥有全部权限",
                1, 1, now, now
        );

        assertThat(role.id()).isEqualTo(1L);
        assertThat(role.name()).isEqualTo("超级管理员");
        assertThat(role.code()).isEqualTo("admin");
        assertThat(role.description()).isEqualTo("拥有全部权限");
        assertThat(role.status()).isEqualTo(1);
        assertThat(role.sortOrder()).isEqualTo(1);
        assertThat(role.createTime()).isEqualTo(now);
        assertThat(role.updateTime()).isEqualTo(now);
    }

    @Test
    void saveRequestShouldRejectBlankName() {
        RoleSaveRequest request = new RoleSaveRequest(
                "", "admin", "拥有全部权限", 1, 1
        );

        assertThat(validator.validate(request))
                .anyMatch(violation -> violation.getMessage().equals("角色名称不能为空"));
    }

    @Test
    void saveRequestShouldRejectBlankCode() {
        RoleSaveRequest request = new RoleSaveRequest(
                "超级管理员", "", "拥有全部权限", 1, 1
        );

        assertThat(validator.validate(request))
                .anyMatch(violation -> violation.getMessage().equals("角色编码不能为空"));
    }

    @Test
    void queryRequestShouldProvideDefaultPageAndSize() {
        RoleQueryRequest request = new RoleQueryRequest(null, null, null, null);

        assertThat(request.pageOrDefault()).isEqualTo(1);
        assertThat(request.sizeOrDefault()).isEqualTo(10);
    }

    @Test
    void roleVoShouldHoldDisplayFields() {
        LocalDateTime now = LocalDateTime.now();

        RoleVO roleVO = new RoleVO(
                1L, "超级管理员", "admin", "拥有全部权限",
                1, 1, now
        );

        assertThat(roleVO.name()).isEqualTo("超级管理员");
        assertThat(roleVO.code()).isEqualTo("admin");
        assertThat(roleVO.createTime()).isEqualTo(now);
    }

    @Test
    void menuAssignRequestShouldRejectNullMenuIds() {
        RoleMenuAssignRequest request = new RoleMenuAssignRequest(null);

        assertThat(validator.validate(request))
                .anyMatch(violation -> violation.getMessage().equals("菜单ID列表不能为空"));
    }

    @Test
    void menuAssignRequestShouldHoldMenuIds() {
        RoleMenuAssignRequest request = new RoleMenuAssignRequest(List.of(1L, 2L, 3L));

        assertThat(request.menuIds()).containsExactly(1L, 2L, 3L);
    }
}
