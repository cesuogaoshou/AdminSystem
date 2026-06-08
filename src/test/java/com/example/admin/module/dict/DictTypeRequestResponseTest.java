package com.example.admin.module.dict;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DictTypeRequestResponseTest {

    @Test
    void dictTypeShouldHoldTableFields() {
        LocalDateTime now = LocalDateTime.of(2026, 6, 8, 18, 0);
        DictType dictType = new DictType(
                1L,
                "用户性别",
                "gender",
                "用户性别字典",
                1,
                now,
                now
        );

        assertThat(dictType.id()).isEqualTo(1L);
        assertThat(dictType.name()).isEqualTo("用户性别");
        assertThat(dictType.code()).isEqualTo("gender");
        assertThat(dictType.description()).isEqualTo("用户性别字典");
        assertThat(dictType.status()).isOne();
        assertThat(dictType.createTime()).isEqualTo(now);
        assertThat(dictType.updateTime()).isEqualTo(now);
    }

    @Test
    void dictTypeSaveRequestShouldHoldInputFields() {
        DictTypeSaveRequest request = new DictTypeSaveRequest(
                "业务状态",
                "biz_status",
                "业务状态字典",
                1
        );

        assertThat(request.name()).isEqualTo("业务状态");
        assertThat(request.code()).isEqualTo("biz_status");
        assertThat(request.description()).isEqualTo("业务状态字典");
        assertThat(request.status()).isOne();
    }
}
