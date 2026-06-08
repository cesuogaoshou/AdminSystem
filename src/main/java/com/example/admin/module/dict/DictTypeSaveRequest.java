package com.example.admin.module.dict;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DictTypeSaveRequest(
        @NotBlank(message = "字典名称不能为空")
        @Size(max = 50, message = "字典名称长度不能超过50")
        String name,

        @NotBlank(message = "字典编码不能为空")
        @Size(max = 50, message = "字典编码长度不能超过50")
        String code,

        @Size(max = 200, message = "描述长度不能超过200")
        String description,

        Integer status
) {
}
