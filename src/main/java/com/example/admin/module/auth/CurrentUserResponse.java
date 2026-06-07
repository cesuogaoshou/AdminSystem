package com.example.admin.module.auth;

import java.util.List;

public record CurrentUserResponse(
        Long userId,
        String username,
        List<String> permissions
) {
}
