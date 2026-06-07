package com.example.admin.module.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserRequestResponseTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void userSaveRequestShouldPassValidationWithValidFields() {
        UserSaveRequest request = new UserSaveRequest(
                "admin",
                "超级管理员",
                "admin@example.com",
                "10000000000",
                0,
                "https://example.com/avatar.png",
                2L,
                1
        );

        Set<ConstraintViolation<UserSaveRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void userSaveRequestShouldRejectBlankUsername() {
        UserSaveRequest request = new UserSaveRequest(
                "",
                "超级管理员",
                "admin@example.com",
                "10000000000",
                0,
                null,
                2L,
                1
        );

        Set<ConstraintViolation<UserSaveRequest>> violations = validator.validate(request);

        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("用户名不能为空");
    }

    @Test
    void userSaveRequestShouldRejectInvalidEmail() {
        UserSaveRequest request = new UserSaveRequest(
                "admin",
                "超级管理员",
                "invalid-email",
                "10000000000",
                0,
                null,
                2L,
                1
        );

        Set<ConstraintViolation<UserSaveRequest>> violations = validator.validate(request);

        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("邮箱格式不正确");
    }

    @Test
    void userQueryRequestShouldUseDefaultPageAndSize() {
        UserQueryRequest request = new UserQueryRequest(null, null, null, null, null);

        assertThat(request.pageOrDefault()).isEqualTo(1);
        assertThat(request.sizeOrDefault()).isEqualTo(10);
    }

    @Test
    void userQueryRequestShouldKeepValidPageAndSize() {
        UserQueryRequest request = new UserQueryRequest(2, 20, "admin", 1, 2L);

        assertThat(request.pageOrDefault()).isEqualTo(2);
        assertThat(request.sizeOrDefault()).isEqualTo(20);
        assertThat(request.username()).isEqualTo("admin");
        assertThat(request.status()).isEqualTo(1);
        assertThat(request.deptId()).isEqualTo(2L);
    }

    @Test
    void userVoShouldNotContainPasswordAndShouldKeepDeptName() {
        LocalDateTime now = LocalDateTime.now();

        UserVO vo = new UserVO(
                1L,
                "admin",
                "超级管理员",
                "admin@example.com",
                "10000000000",
                0,
                null,
                2L,
                "技术部",
                1,
                now
        );

        assertThat(vo.id()).isEqualTo(1L);
        assertThat(vo.username()).isEqualTo("admin");
        assertThat(vo.deptName()).isEqualTo("技术部");
        assertThat(vo.createTime()).isEqualTo(now);
    }
    @Test
    void roleAssignRequestShouldRejectNullRoleIds() {
        UserRoleAssignRequest request = new UserRoleAssignRequest(null);

        assertThat(validator.validate(request))
                .anyMatch(violation -> violation.getMessage().equals("角色ID列表不能为空"));
    }

    @Test
    void roleAssignRequestShouldHoldRoleIds() {
        UserRoleAssignRequest request = new UserRoleAssignRequest(List.of(1L, 2L));

        assertThat(request.roleIds()).containsExactly(1L, 2L);
    }
}
