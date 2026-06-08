package com.example.admin.module.dict;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DictItemRequestResponseTest {

    @Test
    void dictItemShouldHoldTableFields() {
        LocalDateTime now = LocalDateTime.of(2026, 6, 8, 19, 0);
        DictItem dictItem = new DictItem(
                1L,
                1L,
                "男",
                "1",
                "blue",
                1,
                1,
                now,
                now
        );

        assertThat(dictItem.id()).isEqualTo(1L);
        assertThat(dictItem.typeId()).isEqualTo(1L);
        assertThat(dictItem.label()).isEqualTo("男");
        assertThat(dictItem.value()).isEqualTo("1");
        assertThat(dictItem.color()).isEqualTo("blue");
        assertThat(dictItem.sortOrder()).isEqualTo(1);
        assertThat(dictItem.status()).isOne();
        assertThat(dictItem.createTime()).isEqualTo(now);
        assertThat(dictItem.updateTime()).isEqualTo(now);
    }

    @Test
    void dictItemSaveRequestShouldHoldInputFields() {
        DictItemSaveRequest request = new DictItemSaveRequest(
                "处理中",
                "processing",
                "blue",
                1,
                1
        );

        assertThat(request.label()).isEqualTo("处理中");
        assertThat(request.value()).isEqualTo("processing");
        assertThat(request.color()).isEqualTo("blue");
        assertThat(request.sortOrder()).isEqualTo(1);
        assertThat(request.status()).isOne();
    }
}
