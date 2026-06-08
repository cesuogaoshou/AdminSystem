package com.example.admin.module.log;

import org.springframework.stereotype.Service;

@Service
public class LogService {

    private final LogMapper logMapper;

    public LogService(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    public void save(SysLog sysLog) {
        logMapper.insert(sysLog);
    }
}
