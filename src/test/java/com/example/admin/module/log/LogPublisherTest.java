package com.example.admin.module.log;

import com.example.admin.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LogPublisherTest {

    @Test
    void publishShouldSendSysLogToRabbitMQ() {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        LogPublisher logPublisher = new LogPublisher(rabbitTemplate);
        SysLog sysLog = sysLog();

        logPublisher.publish(sysLog);

        verify(rabbitTemplate).convertAndSend(
                RabbitMQConfig.SYS_LOG_EXCHANGE,
                RabbitMQConfig.SYS_LOG_ROUTING_KEY,
                sysLog
        );
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
