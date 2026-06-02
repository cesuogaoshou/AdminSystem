package com.example.admin.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import static org.assertj.core.api.Assertions.assertThat;

class RabbitMQConfigTest {

    @Test
    void sysLogQueueShouldUseExpectedName() {
        RabbitMQConfig config = new RabbitMQConfig();

        Queue queue = config.sysLogQueue();

        assertThat(queue.getName()).isEqualTo(RabbitMQConfig.SYS_LOG_QUEUE);
        assertThat(queue.isDurable()).isTrue();
    }

    @Test
    void sysLogExchangeShouldUseExpectedName() {
        RabbitMQConfig config = new RabbitMQConfig();

        DirectExchange exchange = config.sysLogExchange();

        assertThat(exchange.getName()).isEqualTo(RabbitMQConfig.SYS_LOG_EXCHANGE);
        assertThat(exchange.isDurable()).isTrue();
    }

    @Test
    void sysLogBindingShouldBindQueueToExchangeWithRoutingKey() {
        RabbitMQConfig config = new RabbitMQConfig();

        Binding binding = config.sysLogBinding(config.sysLogQueue(), config.sysLogExchange());

        assertThat(binding.getDestination()).isEqualTo(RabbitMQConfig.SYS_LOG_QUEUE);
        assertThat(binding.getExchange()).isEqualTo(RabbitMQConfig.SYS_LOG_EXCHANGE);
        assertThat(binding.getRoutingKey()).isEqualTo(RabbitMQConfig.SYS_LOG_ROUTING_KEY);
    }
}