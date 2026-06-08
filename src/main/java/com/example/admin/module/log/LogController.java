package com.example.admin.module.log;

import com.example.admin.common.PageResult;
import com.example.admin.common.Result;
import com.example.admin.security.RequirePermission;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    @RequirePermission("sys:log:list")
    public Result<PageResult<LogVO>> page(@ModelAttribute LogQueryRequest query) {
        return Result.ok(logService.page(query));
    }
}
