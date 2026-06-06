package com.example.admin.module.dept;

import com.example.admin.common.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptService {

    private final DeptMapper deptMapper;

    public DeptService(DeptMapper deptMapper) {
        this.deptMapper = deptMapper;
    }

    public void create(DeptSaveRequest request) {
        Dept dept = new Dept(
                null,
                request.parentId() == null ? 0L : request.parentId(),
                request.name(),
                request.leader(),
                request.phone(),
                request.sortOrder() == null ? 0 : request.sortOrder(),
                request.status() == null ? 1 : request.status(),
                null,
                null
        );

        deptMapper.insert(dept);
    }

    public void update(Long id, DeptSaveRequest request) {
        if (id.equals(request.parentId())) {
            throw new BusinessException(400, "上级部门不能是自己");
        }

        Dept existing = deptMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "部门不存在");
        }

        Dept dept = new Dept(
                existing.id(),
                request.parentId() == null ? 0L : request.parentId(),
                request.name(),
                request.leader(),
                request.phone(),
                request.sortOrder() == null ? existing.sortOrder() : request.sortOrder(),
                request.status() == null ? existing.status() : request.status(),
                existing.createTime(),
                existing.updateTime()
        );

        deptMapper.update(dept);
    }

    public void delete(Long id) {
        Dept existing = deptMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "部门不存在");
        }

        if (deptMapper.countByParentId(id) > 0) {
            throw new BusinessException(400, "存在子部门，不能删除");
        }

        if (deptMapper.countUsersByDeptId(id) > 0) {
            throw new BusinessException(400, "部门下存在用户，不能删除");
        }

        deptMapper.deleteById(id);
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