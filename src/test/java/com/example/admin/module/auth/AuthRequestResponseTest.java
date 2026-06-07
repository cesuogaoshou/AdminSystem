package com.example.admin.module.auth;

import com.example.admin.security.JwtProperties;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthRequestResponseTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void loginRequestShouldRejectBlankUsername() {
        LoginRequest request = new LoginRequest("", "123456");

        assertThat(validator.validate(request))
                .anyMatch(violation -> violation.getMessage().equals("用户名不能为空"));
    }

    @Test
    void loginRequestShouldRejectBlankPassword() {
        LoginRequest request = new LoginRequest("admin", "");

        assertThat(validator.validate(request))
                .anyMatch(violation -> violation.getMessage().equals("密码不能为空"));
    }

    @Test
    void loginResponseShouldHoldTokenAndUserInfo() {
        LoginResponse response = new LoginResponse(
                "token-value",
                1L,
                "admin",
                "超级管理员",
                List.of("sys:user:list", "sys:role:list")
        );

        assertThat(response.token()).isEqualTo("token-value");
        assertThat(response.userId()).isEqualTo(1L);
        assertThat(response.username()).isEqualTo("admin");
        assertThat(response.nickname()).isEqualTo("超级管理员");
        assertThat(response.permissions()).containsExactly("sys:user:list", "sys:role:list");
    }

    @Test
    void jwtPropertiesShouldHoldSecretAndExpiration() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("test-secret");
        properties.setExpiration(86400000L);

        assertThat(properties.getSecret()).isEqualTo("test-secret");
        assertThat(properties.getExpiration()).isEqualTo(86400000L);
    }
}
