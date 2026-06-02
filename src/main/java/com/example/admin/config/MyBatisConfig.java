package com.example.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "admin.mybatis.enabled", havingValue = "true", matchIfMissing = true)
@MapperScan("com.example.admin")
public class MyBatisConfig {
}
