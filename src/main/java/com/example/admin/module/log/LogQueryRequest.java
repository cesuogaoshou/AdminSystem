package com.example.admin.module.log;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record LogQueryRequest(
        Integer page,
        Integer size,
        String username,
        String module,
        Integer status,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime startTime,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime endTime
) {

    public int pageOrDefault() {
        return page == null || page < 1 ? 1 : page;
    }

    public int sizeOrDefault() {
        return size == null || size < 1 ? 10 : size;
    }
}
