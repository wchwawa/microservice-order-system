package com.example.store.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatusListener.class);


    @RabbitListener(queues = "order.status.to.store")
    public void processOrderStatus(String message) {
        LOGGER.info(message);
    }

}
