package com.example.admin.module.role;

import com.example.admin.common.PageResult;
import com.example.admin.common.Result;
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

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public Result<PageResult<RoleVO>> page(@ModelAttribute RoleQueryRequest query) {
        return Result.ok(roleService.page(query));
    }

    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable Long id) {
        return Result.ok(roleService.getById(id));
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody RoleSaveRequest request) {
        roleService.create(request);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody RoleSaveRequest request) {
        roleService.update(id, request);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.ok();
    }

    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        roleService.changeStatus(id, status);
        return Result.ok();
    }
}
