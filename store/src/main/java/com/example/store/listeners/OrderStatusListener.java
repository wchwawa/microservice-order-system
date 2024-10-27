package com.example.store.listeners;

import com.example.store.service.DeliveryStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderStatusListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatusListener.class);

    @Autowired
    private DeliveryStatusService deliveryStatusService;

    @RabbitListener(queues = "order.status.to.store")
    public void processOrderStatus(String message) {
        LOGGER.info("Received delivery status update: {}", message);
        try {
            String[] parts = message.split("order | status is updated to ");
            String orderId = parts[1].split(" status")[0].trim();
            String status = parts[2].trim();

            deliveryStatusService.updateDeliveryStatus(orderId, status);
            LOGGER.info("Updated delivery status for order: {}", orderId);
        } catch (Exception e) {
            LOGGER.error("Error processing delivery status: {}", e.getMessage());
        }
    }
}
