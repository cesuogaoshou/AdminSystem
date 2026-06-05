package com.example.admin.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordConfigTest {

    @Test
    void passwordEncoderShouldEncodeAndMatchRawPassword() {
        PasswordConfig config = new PasswordConfig();

        PasswordEncoder passwordEncoder = config.passwordEncoder();
        String encoded = passwordEncoder.encode("123456");

        assertThat(encoded).isNotEqualTo("123456");
        assertThat(passwordEncoder.matches("123456", encoded)).isTrue();
    }
}