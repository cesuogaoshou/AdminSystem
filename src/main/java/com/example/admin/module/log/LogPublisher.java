package com.example.admin.module.log;

import com.example.admin.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class LogPublisher {

    private final RabbitTemplate rabbitTemplate;

    public LogPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(SysLog sysLog) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SYS_LOG_EXCHANGE,
                RabbitMQConfig.SYS_LOG_ROUTING_KEY,
                sysLog
        );
    }
}
