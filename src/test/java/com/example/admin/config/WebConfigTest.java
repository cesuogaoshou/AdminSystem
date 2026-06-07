package com.example.admin.config;

import com.example.admin.security.AuthInterceptor;
import com.example.admin.security.JwtService;
import com.example.admin.security.PermissionInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class WebConfigTest {

    @Test
    void webConfigShouldBeCreated() {
        WebConfig webConfig = new WebConfig();

        assertThat(webConfig).isNotNull();
    }

    @Test
    void addInterceptorsShouldRegisterSecurityInterceptors() {
        JwtService jwtService = mock(JwtService.class);
        WebConfig webConfig = new WebConfig();
        ReflectionTestUtils.setField(webConfig, "jwtService", jwtService);
        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        InterceptorRegistration authRegistration = mock(InterceptorRegistration.class);
        InterceptorRegistration permissionRegistration = mock(InterceptorRegistration.class);
        org.mockito.Mockito.when(registry.addInterceptor(any(AuthInterceptor.class))).thenReturn(authRegistration);
        org.mockito.Mockito.when(registry.addInterceptor(any(PermissionInterceptor.class))).thenReturn(permissionRegistration);
        org.mockito.Mockito.when(authRegistration.addPathPatterns("/api/**")).thenReturn(authRegistration);
        org.mockito.Mockito.when(permissionRegistration.addPathPatterns("/api/**")).thenReturn(permissionRegistration);

        webConfig.addInterceptors(registry);

        verify(registry).addInterceptor(any(AuthInterceptor.class));
        verify(registry).addInterceptor(any(PermissionInterceptor.class));
    }

    @Test
    void addInterceptorsShouldSkipWhenJwtServiceMissing() {
        WebConfig webConfig = new WebConfig();
        InterceptorRegistry registry = mock(InterceptorRegistry.class);

        webConfig.addInterceptors(registry);

        verify(registry, never()).addInterceptor(org.mockito.ArgumentMatchers.any());
    }
}
