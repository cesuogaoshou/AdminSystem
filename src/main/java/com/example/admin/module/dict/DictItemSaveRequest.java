package com.example.admin.module.dict;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DictItemSaveRequest(
        @NotBlank(message = "字典标签不能为空")
        @Size(max = 50, message = "字典标签长度不能超过50")
        String label,

        @NotBlank(message = "字典值不能为空")
        @Size(max = 50, message = "字典值长度不能超过50")
        String value,

        @Size(max = 20, message = "颜色长度不能超过20")
        String color,

        Integer sortOrder,

        Integer status
) {
}
