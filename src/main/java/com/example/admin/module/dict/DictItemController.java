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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DictItemController {

    private final DictItemService dictItemService;

    public DictItemController(DictItemService dictItemService) {
        this.dictItemService = dictItemService;
    }

    @GetMapping("/api/dict-types/{typeId}/items")
    @RequirePermission("sys:dict:list")
    public Result<List<DictItem>> listByTypeId(@PathVariable Long typeId) {
        return Result.ok(dictItemService.listByTypeId(typeId));
    }

    @GetMapping("/api/dict-items")
    public Result<List<DictItem>> listByTypeCode(@RequestParam String typeCode) {
        return Result.ok(dictItemService.listByTypeCode(typeCode));
    }

    @PostMapping("/api/dict-types/{typeId}/items")
    @RequirePermission("sys:dict:add")
    @OperationLog(module = "字典管理", operation = "新增字典项")
    public Result<Void> create(@PathVariable Long typeId, @Valid @RequestBody DictItemSaveRequest request) {
        dictItemService.create(typeId, request);
        return Result.ok();
    }

    @PutMapping("/api/dict-items/{id}")
    @RequirePermission("sys:dict:update")
    @OperationLog(module = "字典管理", operation = "修改字典项")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody DictItemSaveRequest request) {
        dictItemService.update(id, request);
        return Result.ok();
    }

    @DeleteMapping("/api/dict-items/{id}")
    @RequirePermission("sys:dict:delete")
    @OperationLog(module = "字典管理", operation = "删除字典项")
    public Result<Void> delete(@PathVariable Long id) {
        dictItemService.delete(id);
        return Result.ok();
    }
}
