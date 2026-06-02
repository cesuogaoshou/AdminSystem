package com.example.admin.module.user;

public record UserQueryRequest(
        Integer page,
        Integer size,
        String username,
        Integer status,
        Long deptId
) {

    public int pageOrDefault() {
        return page == null || page < 1 ? 1 : page;
    }

    public int sizeOrDefault() {
        return size == null || size < 1 ? 10 : size;
    }
}