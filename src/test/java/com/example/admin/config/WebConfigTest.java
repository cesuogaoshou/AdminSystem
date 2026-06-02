package com.example.admin.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WebConfigTest {

    @Test
    void webConfigShouldBeCreated() {
        WebConfig webConfig = new WebConfig();

        assertThat(webConfig).isNotNull();
    }
}