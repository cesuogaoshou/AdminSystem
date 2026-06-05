package com.example.admin.module.dept;

import java.util.List;

public record DeptVO(
        Long id,
        Long parentId,
        String name,
        String leader,
        String phone,
        Integer sortOrder,
        Integer status,
        List<DeptVO> children
) {
}