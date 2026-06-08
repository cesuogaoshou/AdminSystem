package com.example.admin.module.log;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class OperationLogTest {

    @Test
    void operationLogShouldKeepModuleAndOperationAtRuntime() throws NoSuchMethodException {
        Method method = TestController.class.getDeclaredMethod("createUser");

        OperationLog operationLog = method.getAnnotation(OperationLog.class);

        assertThat(operationLog).isNotNull();
        assertThat(operationLog.module()).isEqualTo("用户管理");
        assertThat(operationLog.operation()).isEqualTo("新增用户");
    }

    private static final class TestController {

        @OperationLog(module = "用户管理", operation = "新增用户")
        void createUser() {
        }
    }
}
