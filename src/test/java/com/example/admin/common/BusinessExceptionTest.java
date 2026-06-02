package com.example.admin.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessExceptionTest {

    @Test
    void constructorWithCodeAndMessageShouldKeepBothValues() {
        BusinessException exception = new BusinessException(400, "bad request");

        assertThat(exception.getCode()).isEqualTo(400);
        assertThat(exception.getMessage()).isEqualTo("bad request");
    }

    @Test
    void constructorWithMessageShouldUseDefaultServerErrorCode() {
        BusinessException exception = new BusinessException("server error");

        assertThat(exception.getCode()).isEqualTo(500);
        assertThat(exception.getMessage()).isEqualTo("server error");
    }
}