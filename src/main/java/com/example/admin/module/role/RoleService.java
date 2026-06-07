package com.example.admin.module.role;

import com.example.admin.common.BusinessException;
import com.example.admin.common.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleMapper roleMapper;

    public RoleService(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public Role getById(Long id) {
        Role role = roleMapper.findById(id);
        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }
        return role;
    }

    public PageResult<RoleVO> page(RoleQueryRequest query) {
        PageHelper.startPage(query.pageOrDefault(), query.sizeOrDefault());
        try {
            List<RoleVO> rows = roleMapper.findPage(query);
            PageInfo<RoleVO> pageInfo = new PageInfo<>(rows);
            return PageResult.of(pageInfo.getTotal(), rows);
        } finally {
            PageHelper.clearPage();
        }
    }

    public void create(RoleSaveRequest request) {
        if (roleMapper.countByCode(request.code()) > 0) {
            throw new BusinessException(400, "角色编码已存在");
        }

        Role role = new Role(
                null,
                request.name(),
                request.code(),
                request.description(),
                request.status() == null ? 1 : request.status(),
                request.sortOrder() == null ? 0 : request.sortOrder(),
                null,
                null
        );

        roleMapper.insert(role);
    }

    public void update(Long id, RoleSaveRequest request) {
        Role existing = getById(id);
        Role duplicate = roleMapper.findByCode(request.code());
        if (duplicate != null && !duplicate.id().equals(id)) {
            throw new BusinessException(400, "角色编码已存在");
        }

        Role role = new Role(
                existing.id(),
                request.name(),
                request.code(),
                request.description(),
                request.status() == null ? existing.status() : request.status(),
                request.sortOrder() == null ? existing.sortOrder() : request.sortOrder(),
                existing.createTime(),
                existing.updateTime()
        );

        roleMapper.update(role);
    }

    public void delete(Long id) {
        getById(id);

        if (roleMapper.countUsersByRoleId(id) > 0) {
            throw new BusinessException(400, "角色已分配给用户，不能删除");
        }

        roleMapper.deleteById(id);
    }

    public void changeStatus(Long id, Integer status) {
        if (status == null || (status != 0 && status != 1)) {
            throw new BusinessException(400, "角色状态不正确");
        }

        getById(id);
        roleMapper.updateStatus(id, status);
    }

    public List<Long> getMenuIds(Long id) {
        getById(id);
        return roleMapper.findMenuIdsByRoleId(id);
    }

    public void assignMenus(Long id, RoleMenuAssignRequest request) {
        getById(id);

        roleMapper.deleteMenusByRoleId(id);
        if (!request.menuIds().isEmpty()) {
            roleMapper.insertRoleMenus(id, request.menuIds());
        }
    }
}
