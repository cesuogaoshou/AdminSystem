package com.example.admin.module.user;

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

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Result<PageResult<UserVO>> page(@ModelAttribute UserQueryRequest query) {
        return Result.ok(userService.page(query));
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return Result.ok(userService.getById(id));
    }

    @PostMapping
    public Result<Void> create(@Valid @RequestBody UserSaveRequest request) {
        userService.create(request);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody UserSaveRequest request) {
        userService.update(id, request);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.ok();
    }

    @PutMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.changeStatus(id, status);
        return Result.ok();
    }

    @GetMapping("/{id}/roles")
    public Result<List<Long>> getRoleIds(@PathVariable Long id) {
        return Result.ok(userService.getRoleIds(id));
    }

    @PutMapping("/{id}/roles")
    public Result<Void> assignRoles(@PathVariable Long id, @Valid @RequestBody UserRoleAssignRequest request) {
        userService.assignRoles(id, request);
        return Result.ok();
    }

    @GetMapping("/{id}/permissions")
    public Result<List<String>> getPermissions(@PathVariable Long id) {
        return Result.ok(userService.getPermissions(id));
    }
}
