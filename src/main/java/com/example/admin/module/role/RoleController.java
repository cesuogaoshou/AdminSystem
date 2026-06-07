package com.example.admin.module.role;

import com.example.admin.common.PageResult;
import com.example.admin.common.Result;
import com.example.admin.security.RequirePermission;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @RequirePermission("sys:role:list")
    public Result<PageResult<RoleVO>> page(@ModelAttribute RoleQueryRequest query) {
        return Result.ok(roleService.page(query));
    }

    @GetMapping("/{id}")
    @RequirePermission("sys:role:list")
    public Result<Role> getById(@PathVariable Long id) {
        return Result.ok(roleService.getById(id));
    }

    @PostMapping
    @RequirePermission("sys:role:add")
    public Result<Void> create(@Valid @RequestBody RoleSaveRequest request) {
        roleService.create(request);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @RequirePermission("sys:role:update")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody RoleSaveRequest request) {
        roleService.update(id, request);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @RequirePermission("sys:role:delete")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.ok();
    }

    @PutMapping("/{id}/status")
    @RequirePermission("sys:role:update")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        roleService.changeStatus(id, status);
        return Result.ok();
    }

    @GetMapping("/{id}/menus")
    @RequirePermission("sys:role:list")
    public Result<List<Long>> getMenuIds(@PathVariable Long id) {
        return Result.ok(roleService.getMenuIds(id));
    }

    @PutMapping("/{id}/menus")
    @RequirePermission("sys:role:update")
    public Result<Void> assignMenus(@PathVariable Long id, @Valid @RequestBody RoleMenuAssignRequest request) {
        roleService.assignMenus(id, request);
        return Result.ok();
    }
}
