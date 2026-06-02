package com.example.admin.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultTest {

    @Test
    void okWithDataShouldReturnSuccessResult() {
        Result<String> result = Result.ok("hello");

        assertThat(result.code()).isEqualTo(200);
        assertThat(result.message()).isEqualTo("success");
        assertThat(result.data()).isEqualTo("hello");
    }

    @Test
    void okWithoutDataShouldReturnSuccessResultWithNullData() {
        Result<Void> result = Result.ok();

        assertThat(result.code()).isEqualTo(200);
        assertThat(result.message()).isEqualTo("success");
        assertThat(result.data()).isNull();
    }

    @Test
    void failWithCodeAndMessageShouldReturnFailureResult() {
        Result<Void> result = Result.fail(400, "bad request");

        assertThat(result.code()).isEqualTo(400);
        assertThat(result.message()).isEqualTo("bad request");
        assertThat(result.data()).isNull();
    }

    @Test
    void failWithMessageShouldReturnDefaultServerErrorResult() {
        Result<Void> result = Result.fail("server error");

        assertThat(result.code()).isEqualTo(500);
        assertThat(result.message()).isEqualTo("server error");
        assertThat(result.data()).isNull();
    }
}