package com.example.admin.module.auth;

import java.util.List;

public record LoginResponse(
        String token,
        Long userId,
        String username,
        String nickname,
        List<String> permissions
) {
}
