package com.example.admin.common;

public record Result<T>(
        int code,
        String message,
        T data
) {

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }

    public static Result<Void> ok() {
        return new Result<>(200, "success", null);
    }

    public static Result<Void> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static Result<Void> fail(String message) {
        return new Result<>(500, message, null);
    }
}