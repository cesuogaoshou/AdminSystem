package com.example.admin.config;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.core.annotation.AnnotationUtils;

import static org.assertj.core.api.Assertions.assertThat;

class MyBatisConfigTest {

    @Test
    void myBatisConfigShouldScanAdminPackageMappers() {
        MapperScan mapperScan = AnnotationUtils.findAnnotation(MyBatisConfig.class, MapperScan.class);

        assertThat(mapperScan).isNotNull();
        assertThat(mapperScan.basePackages()).containsExactly("com.example.admin");
    }
}