package com.example.admin.module.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

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
}
