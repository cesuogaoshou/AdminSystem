package com.example.admin.common;

import java.util.List;

public record PageResult<T>(
        long total,
        List<T> rows
) {

    public static <T> PageResult<T> of(long total, List<T> rows) {
        return new PageResult<>(total, rows);
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(0, List.of());
    }
}