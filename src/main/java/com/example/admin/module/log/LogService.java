package com.example.admin.module.log;

import com.example.admin.common.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private final LogMapper logMapper;

    public LogService(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    public void save(SysLog sysLog) {
        logMapper.insert(sysLog);
    }

    public PageResult<LogVO> page(LogQueryRequest query) {
        PageHelper.startPage(query.pageOrDefault(), query.sizeOrDefault());
        try {
            List<LogVO> rows = logMapper.findPage(query);
            PageInfo<LogVO> pageInfo = new PageInfo<>(rows);
            return PageResult.of(pageInfo.getTotal(), rows);
        } finally {
            PageHelper.clearPage();
        }
    }
}
