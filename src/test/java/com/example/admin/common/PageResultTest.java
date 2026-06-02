package com.example.admin.common;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResultTest {

    @Test
    void ofShouldCreatePageResultWithTotalAndRows() {
        List<String> rows = List.of("admin", "user");

        PageResult<String> result = PageResult.of(2, rows);

        assertThat(result.total()).isEqualTo(2);
        assertThat(result.rows()).containsExactly("admin", "user");
    }

    @Test
    void emptyShouldCreateEmptyPageResult() {
        PageResult<String> result = PageResult.empty();

        assertThat(result.total()).isZero();
        assertThat(result.rows()).isEmpty();
    }
}