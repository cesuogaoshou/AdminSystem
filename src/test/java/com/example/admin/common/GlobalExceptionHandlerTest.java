package com.example.admin.common;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleBusinessExceptionShouldReturnBusinessErrorResult() {
        BusinessException exception = new BusinessException(400, "bad request");

        Result<Void> result = handler.handleBusinessException(exception);

        assertThat(result.code()).isEqualTo(400);
        assertThat(result.message()).isEqualTo("bad request");
        assertThat(result.data()).isNull();
    }

    @Test
    void handleExceptionShouldReturnDefaultServerErrorResult() {
        Exception exception = new Exception("database down");

        Result<Void> result = handler.handleException(exception);

        assertThat(result.code()).isEqualTo(500);
        assertThat(result.message()).isEqualTo("系统异常");
        assertThat(result.data()).isNull();
    }

    @Test
    void handleMethodArgumentNotValidExceptionShouldReturnFirstFieldErrorMessage() throws NoSuchMethodException {
        Method method = TestController.class.getDeclaredMethod("save", TestRequest.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        BindingResult bindingResult = new BeanPropertyBindingResult(new TestRequest(""), "testRequest");
        bindingResult.addError(new FieldError("testRequest", "name", "名称不能为空"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        Result<Void> result = handler.handleMethodArgumentNotValidException(exception);

        assertThat(result.code()).isEqualTo(400);
        assertThat(result.message()).isEqualTo("名称不能为空");
        assertThat(result.data()).isNull();
    }

    private record TestRequest(String name) {
    }

    private static class TestController {
        @SuppressWarnings("unused")
        void save(TestRequest request) {
        }
    }
}