package com.example.admin.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SYS_LOG_QUEUE = "admin.sys.log.queue";
    public static final String SYS_LOG_EXCHANGE = "admin.sys.log.exchange";
    public static final String SYS_LOG_ROUTING_KEY = "admin.sys.log";

    @Bean
    public Queue sysLogQueue() {
        return new Queue(SYS_LOG_QUEUE, true);
    }

    @Bean
    public DirectExchange sysLogExchange() {
        return new DirectExchange(SYS_LOG_EXCHANGE, true, false);
    }

    @Bean
    public Binding sysLogBinding(Queue sysLogQueue, DirectExchange sysLogExchange) {
        return BindingBuilder
                .bind(sysLogQueue)
                .to(sysLogExchange)
                .with(SYS_LOG_ROUTING_KEY);
    }
}