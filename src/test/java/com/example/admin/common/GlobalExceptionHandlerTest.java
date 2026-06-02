package com.example.admin.common;

import org.junit.jupiter.api.Test;

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
}