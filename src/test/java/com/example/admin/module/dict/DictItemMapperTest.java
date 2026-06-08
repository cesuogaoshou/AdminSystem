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
class DictItemMapperTest {

    @Autowired
    private DictItemMapper dictItemMapper;

    @Test
    void findByTypeIdShouldReturnInitialItems() {
        List<DictItem> items = dictItemMapper.findByTypeId(1L);

        assertThat(items)
                .extracting(DictItem::label)
                .contains("未知", "男", "女");
    }

    @Test
    void findByTypeCodeShouldReturnEnabledItems() {
        List<DictItem> items = dictItemMapper.findByTypeCode("gender");

        assertThat(items)
                .extracting(DictItem::label)
                .containsExactly("未知", "男", "女");
    }

    @Test
    void findByIdShouldReturnDictItem() {
        DictItem dictItem = dictItemMapper.findById(1L);

        assertThat(dictItem).isNotNull();
        assertThat(dictItem.typeId()).isEqualTo(1L);
        assertThat(dictItem.label()).isEqualTo("未知");
    }

    @Test
    void findByTypeIdAndValueShouldReturnDictItem() {
        DictItem dictItem = dictItemMapper.findByTypeIdAndValue(1L, "1");

        assertThat(dictItem).isNotNull();
        assertThat(dictItem.label()).isEqualTo("男");
    }

    @Test
    void insertUpdateAndDeleteShouldChangeDictItem() {
        DictItem dictItem = new DictItem(
                900000000001L,
                2L,
                "测试状态",
                "test_status",
                "gray",
                9,
                1,
                null,
                null
        );

        assertThat(dictItemMapper.insert(dictItem)).isEqualTo(1);
        assertThat(dictItemMapper.findByTypeIdAndValue(2L, "test_status")).isNotNull();

        DictItem updated = new DictItem(
                900000000001L,
                2L,
                "测试状态更新",
                "test_status",
                "green",
                10,
                0,
                null,
                null
        );
        assertThat(dictItemMapper.update(updated)).isEqualTo(1);
        assertThat(dictItemMapper.findById(900000000001L).status()).isZero();

        assertThat(dictItemMapper.deleteById(900000000001L)).isEqualTo(1);
        assertThat(dictItemMapper.findById(900000000001L)).isNull();
    }
}
