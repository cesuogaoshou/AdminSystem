package com.example.admin.module.dict;

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
@RequestMapping("/api/dict-types")
public class DictTypeController {

    private final DictTypeService dictTypeService;

    public DictTypeController(DictTypeService dictTypeService) {
        this.dictTypeService = dictTypeService;
    }

    @GetMapping
    @RequirePermission("sys:dict:list")
    public Result<List<DictType>> list() {
        return Result.ok(dictTypeService.list());
    }

    @GetMapping("/{id}")
    @RequirePermission("sys:dict:list")
    public Result<DictType> getById(@PathVariable Long id) {
        return Result.ok(dictTypeService.getById(id));
    }

    @PostMapping
    @RequirePermission("sys:dict:add")
    @OperationLog(module = "字典管理", operation = "新增字典类型")
    public Result<Void> create(@Valid @RequestBody DictTypeSaveRequest request) {
        dictTypeService.create(request);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @RequirePermission("sys:dict:update")
    @OperationLog(module = "字典管理", operation = "修改字典类型")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody DictTypeSaveRequest request) {
        dictTypeService.update(id, request);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @RequirePermission("sys:dict:delete")
    @OperationLog(module = "字典管理", operation = "删除字典类型")
    public Result<Void> delete(@PathVariable Long id) {
        dictTypeService.delete(id);
        return Result.ok();
    }
}
