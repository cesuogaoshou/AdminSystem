package com.example.admin.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestExceptionController {

    @PostMapping("/test/business-error")
    Result<Void> businessError() {
        throw new BusinessException(400, "业务错误");
    }

    @PostMapping("/test/validation-error")
    Result<Void> validationError(@Valid @RequestBody TestRequest request) {
        return Result.ok();
    }

    record TestRequest(
            @NotBlank(message = "名称不能为空")
            String name
    ) {
    }
}