package com.example.admin.security;

import java.util.List;

public record CurrentUser(
        Long userId,
        String username,
        List<String> permissions
) {
}
