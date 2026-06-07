package com.example.admin.config;

import com.example.admin.security.AuthInterceptor;
import com.example.admin.security.JwtService;
import com.example.admin.security.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired(required = false)
    private JwtService jwtService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (jwtService == null) {
            return;
        }

        registry.addInterceptor(new AuthInterceptor(jwtService))
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/doc.html",
                        "/webjars/**",
                        "/actuator/**"
                );

        registry.addInterceptor(new PermissionInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/doc.html",
                        "/webjars/**",
                        "/actuator/**"
                );
    }
}
