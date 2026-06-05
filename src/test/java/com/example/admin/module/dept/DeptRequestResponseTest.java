package com.example.admin.module.dept;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeptRequestResponseTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deptShouldHoldAllFields() {
        LocalDateTime now = LocalDateTime.now();

        Dept dept = new Dept(
                1L, 0L, "总公司", "CEO", "10000000000",
                1, 1, now, now
        );

        assertThat(dept.id()).isEqualTo(1L);
        assertThat(dept.parentId()).isZero();
        assertThat(dept.name()).isEqualTo("总公司");
        assertThat(dept.leader()).isEqualTo("CEO");
        assertThat(dept.phone()).isEqualTo("10000000000");
        assertThat(dept.sortOrder()).isEqualTo(1);
        assertThat(dept.status()).isEqualTo(1);
        assertThat(dept.createTime()).isEqualTo(now);
        assertThat(dept.updateTime()).isEqualTo(now);
    }

    @Test
    void saveRequestShouldRejectBlankName() {
        DeptSaveRequest request = new DeptSaveRequest(
                0L, "", "CEO", "10000000000", 1, 1
        );

        assertThat(validator.validate(request))
                .anyMatch(violation -> violation.getMessage().equals("部门名称不能为空"));
    }

    @Test
    void deptVoShouldHoldChildren() {
        DeptVO child = new DeptVO(
                2L, 1L, "技术部", "CTO", "10000000001",
                2, 1, List.of()
        );
        DeptVO root = new DeptVO(
                1L, 0L, "总公司", "CEO", "10000000000",
                1, 1, List.of(child)
        );

        assertThat(root.children()).containsExactly(child);
        assertThat(root.children().getFirst().name()).isEqualTo("技术部");
    }
}