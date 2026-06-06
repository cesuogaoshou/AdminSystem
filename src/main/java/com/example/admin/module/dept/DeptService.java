package com.example.admin.module.dept;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptService {

    private final DeptMapper deptMapper;

    public DeptService(DeptMapper deptMapper) {
        this.deptMapper = deptMapper;
    }

    public List<DeptVO> tree() {
        List<Dept> depts = deptMapper.findAll();

        return depts.stream()
                .filter(dept -> dept.parentId() == 0)
                .map(dept -> toTreeNode(dept, depts))
                .toList();
    }

    private DeptVO toTreeNode(Dept dept, List<Dept> allDepts) {
        List<DeptVO> children = allDepts.stream()
                .filter(child -> dept.id().equals(child.parentId()))
                .map(child -> toTreeNode(child, allDepts))
                .toList();

        return new DeptVO(
                dept.id(),
                dept.parentId(),
                dept.name(),
                dept.leader(),
                dept.phone(),
                dept.sortOrder(),
                dept.status(),
                children
        );
    }
}