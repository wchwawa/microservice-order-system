package com.example.deliveryservice.service;

import com.example.deliveryservice.model.MailRequest;
import com.example.deliveryservice.model.deliveryrequest;
import com.example.deliveryservice.model.deliverystatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
// service communication using RestTemplate
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Random;

import java.util.concurrent.TimeUnit;

@Service
public class deliveryservice {

    private static final Logger logger = LoggerFactory.getLogger(deliveryservice.class);
    private final Random random = new Random();
    @Autowired
    private RestTemplate restTemplate;

    @Value("${email.service.url}")
    private String emailServiceUrl;

    // Mail Service 的 URL
    private static final String MAIL_SERVICE_URL = "http://localhost:8082/emails/send";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void processDelivery(deliveryrequest request) {
        try {
            // 1. send email to customer
            notifyStore(request.getOrderId(), deliverystatus.RECEIVED);
            sendEmail(request.getCustomerEmail(), "order received",
                    "your order" + request.getOrderId() + " order received, we will process it soon.");

            // mocking order picking
            TimeUnit.SECONDS.sleep(2);
            notifyStore(request.getOrderId(), deliverystatus.PICKED_UP);
            sendEmail(request.getCustomerEmail(), "order picked up",
                    "your order" + request.getOrderId() + " has been picked up and is on its way.");

            // mocking order in transit
            TimeUnit.SECONDS.sleep(2);
            notifyStore(request.getOrderId(), deliverystatus.IN_TRANSIT);
            sendEmail(request.getCustomerEmail(), "order in transit",
                    "hanged tight! your order" + request.getOrderId() + " is on its way!");
            if (random.nextDouble() < 0.30){
                throw new InterruptedException("order lost during transit");
            }
                // mocking order delivered
            TimeUnit.SECONDS.sleep(2);
            notifyStore(request.getOrderId(), deliverystatus.DELIVERED);
            sendEmail(request.getCustomerEmail(), "order delivered",
                    "your order" + request.getOrderId() + " has been delivered, thank you for your purchase!");
        } catch (InterruptedException e) {
            logger.error("there was an error processing the order: {}", e.getMessage());
            notifyStore(request.getOrderId(), deliverystatus.FAILED);
            sendEmail(request.getCustomerEmail(), "order processing failed",
                    "sorry，your order " + request.getOrderId() + "processing is failed, the order has been cancelled.");
        } catch (Exception e) {
            logger.error("unknown error on order: {}", e.getMessage());
            notifyStore(request.getOrderId(), deliverystatus.FAILED);
            sendEmail(request.getCustomerEmail(), "order processing failed",
                    "sorry，your order " + request.getOrderId() + "processing is failed, the order has been cancelled.");
        }
    }

    private void notifyStore(String orderId, deliverystatus status) {
        String message = "notify Store：order " + orderId + " status is updated to " + status;
        logger.info(message);
        // send message via message queue to store service
        rabbitTemplate.convertAndSend("order.status.to.store", message);
    }

    private void sendEmail(String recipient, String subject, String message) {

        String emailApiUrl = emailServiceUrl + "/emails/send";

        // create a MailRequest object
        MailRequest mailRequest = new MailRequest();
        mailRequest.setRecipient(recipient);
        mailRequest.setSubject(subject);
        mailRequest.setMessage(message);

        // sending post request to mail service using restTemplate
        restTemplate.postForEntity(MAIL_SERVICE_URL, mailRequest, String.class);
    }

}
