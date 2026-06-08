package com.example.admin.module.dict;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MapperScan("com.example.admin.module.dict")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/admin_system?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
        "spring.datasource.username=root",
        "spring.datasource.password=${ADMIN_DB_PASSWORD}",
        "spring.flyway.enabled=false"
})
class DictTypeMapperTest {

    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Test
    void findAllShouldReturnInitialDictTypes() {
        List<DictType> dictTypes = dictTypeMapper.findAll();

        assertThat(dictTypes)
                .extracting(DictType::code)
                .contains("gender", "status");
    }

    @Test
    void findByIdShouldReturnDictType() {
        DictType dictType = dictTypeMapper.findById(1L);

        assertThat(dictType).isNotNull();
        assertThat(dictType.code()).isEqualTo("gender");
        assertThat(dictType.status()).isOne();
    }

    @Test
    void findByCodeShouldReturnDictType() {
        DictType dictType = dictTypeMapper.findByCode("status");

        assertThat(dictType).isNotNull();
        assertThat(dictType.name()).isEqualTo("通用状态");
    }

    @Test
    void insertUpdateAndDeleteShouldChangeDictType() {
        DictType dictType = new DictType(
                900000000001L,
                "测试字典",
                "test_dict_type",
                "测试字典描述",
                1,
                null,
                null
        );

        assertThat(dictTypeMapper.insert(dictType)).isEqualTo(1);
        assertThat(dictTypeMapper.findByCode("test_dict_type")).isNotNull();

        DictType updated = new DictType(
                900000000001L,
                "测试字典更新",
                "test_dict_type",
                "更新描述",
                0,
                null,
                null
        );
        assertThat(dictTypeMapper.update(updated)).isEqualTo(1);
        assertThat(dictTypeMapper.findById(900000000001L).status()).isZero();

        assertThat(dictTypeMapper.deleteById(900000000001L)).isEqualTo(1);
        assertThat(dictTypeMapper.findById(900000000001L)).isNull();
    }
}
