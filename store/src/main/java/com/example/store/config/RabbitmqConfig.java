package com.example.store.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class RabbitmqConfig {
    @Bean
    public Queue orderStateToStoreQueue() {
        return new Queue("order.status.to.store", true);
    }

}
