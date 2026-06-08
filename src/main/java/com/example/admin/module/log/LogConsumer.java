package com.example.admin.module.log;

import com.example.admin.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LogConsumer {

    private final LogService logService;

    public LogConsumer(LogService logService) {
        this.logService = logService;
    }

    @RabbitListener(queues = RabbitMQConfig.SYS_LOG_QUEUE)
    public void handleSysLog(SysLog sysLog) {
        logService.save(sysLog);
    }
}
