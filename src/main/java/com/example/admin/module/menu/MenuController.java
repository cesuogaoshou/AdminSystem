package com.example.admin.module.menu;

import com.example.admin.common.Result;
import com.example.admin.module.log.OperationLog;
import com.example.admin.security.RequirePermission;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/tree")
    @RequirePermission("sys:menu:list")
    public Result<List<MenuVO>> tree() {
        return Result.ok(menuService.tree());
    }

    @PostMapping
    @RequirePermission("sys:menu:add")
    @OperationLog(module = "菜单管理", operation = "新增菜单")
    public Result<Void> create(@Valid @RequestBody MenuSaveRequest request) {
        menuService.create(request);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @RequirePermission("sys:menu:update")
    @OperationLog(module = "菜单管理", operation = "修改菜单")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody MenuSaveRequest request) {
        menuService.update(id, request);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @RequirePermission("sys:menu:delete")
    @OperationLog(module = "菜单管理", operation = "删除菜单")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.ok();
    }
}
