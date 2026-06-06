package com.example.admin.module.role;

public record RoleQueryRequest(
        Integer page,
        Integer size,
        String name,
        Integer status
) {

    public int pageOrDefault() {
        return page == null || page < 1 ? 1 : page;
    }

    public int sizeOrDefault() {
        return size == null || size < 1 ? 10 : size;
    }
}
