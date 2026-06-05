package com.example.admin.module.dept;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeptSaveRequest(
        Long parentId,

        @NotBlank(message = "部门名称不能为空")
        @Size(max = 50, message = "部门名称长度不能超过50")
        String name,

        @Size(max = 50, message = "负责人长度不能超过50")
        String leader,

        @Size(max = 20, message = "联系电话长度不能超过20")
        String phone,

        Integer sortOrder,
        Integer status
) {
}