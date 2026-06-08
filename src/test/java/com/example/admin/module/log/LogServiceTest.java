package com.example.admin.module.log;

import com.example.admin.common.PageResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @Mock
    private LogMapper logMapper;

    @InjectMocks
    private LogService logService;

    @Test
    void saveShouldInsertOperationLog() {
        SysLog sysLog = new SysLog(
                null,
                "admin",
                "用户管理",
                "新增用户",
                "POST",
                "/api/users",
                "127.0.0.1",
                "{\"username\":\"test\"}",
                "{\"code\":200}",
                42L,
                1,
                null,
                null
        );

        logService.save(sysLog);

        verify(logMapper).insert(sysLog);
    }

    @Test
    void pageShouldReturnPageResult() {
        LogQueryRequest query = new LogQueryRequest(1, 10, "admin", "用户管理", 1, null, null);
        LogVO logVO = new LogVO(
                1L,
                "admin",
                "用户管理",
                "新增用户",
                "POST",
                "/api/users",
                "127.0.0.1",
                42L,
                1,
                LocalDateTime.now()
        );
        when(logMapper.findPage(query)).thenReturn(List.of(logVO));

        PageResult<LogVO> result = logService.page(query);

        assertThat(result.total()).isEqualTo(1);
        assertThat(result.rows()).containsExactly(logVO);
        verify(logMapper).findPage(query);
    }
}
