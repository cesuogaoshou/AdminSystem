package com.example.admin.module.log;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LogConsumerTest {

    @Test
    void handleSysLogShouldSaveLog() {
        LogService logService = mock(LogService.class);
        LogConsumer logConsumer = new LogConsumer(logService);
        SysLog sysLog = sysLog();

        logConsumer.handleSysLog(sysLog);

        verify(logService).save(sysLog);
    }

    private SysLog sysLog() {
        return new SysLog(
                null,
                "admin",
                "用户管理",
                "新增用户",
                "POST",
                "/api/users",
                "127.0.0.1",
                "[]",
                "{\"code\":200}",
                42L,
                1,
                null,
                null
        );
    }
}
