package com.example.admin.module.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserSaveRequest(
        @NotBlank(message = "用户名不能为空")
        @Size(max = 50, message = "用户名长度不能超过50")
        String username,

        @NotBlank(message = "昵称不能为空")
        @Size(max = 50, message = "昵称长度不能超过50")
        String nickname,

        @Email(message = "邮箱格式不正确")
        @Size(max = 100, message = "邮箱长度不能超过100")
        String email,

        @Size(max = 20, message = "手机号长度不能超过20")
        String phone,

        Integer gender,

        @Size(max = 500, message = "头像地址长度不能超过500")
        String avatar,

        Long deptId,

        Integer status
) {
}