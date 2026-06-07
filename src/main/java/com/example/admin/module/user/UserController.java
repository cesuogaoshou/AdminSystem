package com.example.admin.module.user;

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
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @RequirePermission("sys:user:list")
    public Result<PageResult<UserVO>> page(@ModelAttribute UserQueryRequest query) {
        return Result.ok(userService.page(query));
    }

    @GetMapping("/{id}")
    @RequirePermission("sys:user:list")
    public Result<User> getById(@PathVariable Long id) {
        return Result.ok(userService.getById(id));
    }

    @PostMapping
    @RequirePermission("sys:user:add")
    public Result<Void> create(@Valid @RequestBody UserSaveRequest request) {
        userService.create(request);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @RequirePermission("sys:user:update")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody UserSaveRequest request) {
        userService.update(id, request);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @RequirePermission("sys:user:delete")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.ok();
    }

    @PutMapping("/{id}/status")
    @RequirePermission("sys:user:update")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.changeStatus(id, status);
        return Result.ok();
    }

    @GetMapping("/{id}/roles")
    @RequirePermission("sys:user:list")
    public Result<List<Long>> getRoleIds(@PathVariable Long id) {
        return Result.ok(userService.getRoleIds(id));
    }

    @PutMapping("/{id}/roles")
    @RequirePermission("sys:user:update")
    public Result<Void> assignRoles(@PathVariable Long id, @Valid @RequestBody UserRoleAssignRequest request) {
        userService.assignRoles(id, request);
        return Result.ok();
    }

    @GetMapping("/{id}/permissions")
    @RequirePermission("sys:user:list")
    public Result<List<String>> getPermissions(@PathVariable Long id) {
        return Result.ok(userService.getPermissions(id));
    }
}
