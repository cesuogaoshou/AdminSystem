package com.example.admin.security;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    @Test
    void generateTokenShouldCreateParsableToken() {
        JwtService jwtService = jwtService();

        String token = jwtService.generateToken(
                1L,
                "admin",
                List.of("sys:user:list", "sys:role:list")
        );

        assertThat(token).isNotBlank();
        assertThat(jwtService.getUserId(token)).isEqualTo(1L);
        assertThat(jwtService.getUsername(token)).isEqualTo("admin");
        assertThat(jwtService.getPermissions(token))
                .containsExactly("sys:user:list", "sys:role:list");
    }

    @Test
    void isTokenValidShouldReturnTrueForGeneratedToken() {
        JwtService jwtService = jwtService();

        String token = jwtService.generateToken(1L, "admin", List.of("sys:user:list"));

        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    private JwtService jwtService() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("test-secret-must-be-long-enough-for-hmac-sha256");
        properties.setExpiration(86400000L);

        return new JwtService(properties);
    }
}
