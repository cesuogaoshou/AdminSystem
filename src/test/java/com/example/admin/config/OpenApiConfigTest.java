package com.example.admin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {

    @Test
    void adminSystemOpenAPIShouldConfigureJwtBearerAuth() {
        OpenAPI openAPI = new OpenApiConfig().adminSystemOpenAPI();
        SecurityScheme securityScheme = openAPI.getComponents()
                .getSecuritySchemes()
                .get(OpenApiConfig.BEARER_AUTH);

        assertThat(openAPI.getInfo().getTitle()).isEqualTo("AdminSystem API");
        assertThat(securityScheme.getType()).isEqualTo(SecurityScheme.Type.HTTP);
        assertThat(securityScheme.getScheme()).isEqualTo("bearer");
        assertThat(securityScheme.getBearerFormat()).isEqualTo("JWT");
        assertThat(openAPI.getSecurity().getFirst().get(OpenApiConfig.BEARER_AUTH)).isEmpty();
    }
}
