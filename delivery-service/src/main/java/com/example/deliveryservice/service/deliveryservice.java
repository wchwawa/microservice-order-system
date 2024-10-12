package com.example.deliveryservice.service;

import com.example.deliveryservice.model.deliveryrequest;
import com.example.deliveryservice.model.deliverystatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
// 引入 RestTemplate 以调用 Mail Service
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
public class deliveryservice {

    private static final Logger logger = LoggerFactory.getLogger(deliveryservice.class);

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
            // 1. 送货请求已接收
            notifyStore(request.getOrderId(), deliverystatus.RECEIVED);
            sendEmail(request.getCustomerEmail(), "订单已接收",
                    "您的订单 " + request.getOrderId() + " 已接收，我们将尽快为您发货。");

            // 模拟从仓库取货
            TimeUnit.SECONDS.sleep(2);
            notifyStore(request.getOrderId(), deliverystatus.PICKED_UP);
            sendEmail(request.getCustomerEmail(), "订单已取货",
                    "您的订单 " + request.getOrderId() + " 已从仓库取货，正在运输途中。");

            // 模拟运输中
            TimeUnit.SECONDS.sleep(2);
            notifyStore(request.getOrderId(), deliverystatus.IN_TRANSIT);
            sendEmail(request.getCustomerEmail(), "订单运输中",
                    "您的订单 " + request.getOrderId() + " 正在运输途中，预计即将到达。");

            // 模拟送达完成
            TimeUnit.SECONDS.sleep(2);
            notifyStore(request.getOrderId(), deliverystatus.DELIVERED);
            sendEmail(request.getCustomerEmail(), "订单已送达",
                    "您的订单 " + request.getOrderId() + " 已成功送达，感谢您的购买！");
        } catch (InterruptedException e) {
            logger.error("送货处理过程中发生异常: {}", e.getMessage());
            notifyStore(request.getOrderId(), deliverystatus.FAILED);
            sendEmail(request.getCustomerEmail(), "订单处理失败",
                    "抱歉，您的订单 " + request.getOrderId() + " 处理过程中出现问题，订单已被取消。");
        } catch (Exception e) {
            logger.error("送货处理过程中发生未知异常: {}", e.getMessage());
            notifyStore(request.getOrderId(), deliverystatus.FAILED);
            sendEmail(request.getCustomerEmail(), "订单处理失败",
                    "抱歉，您的订单 " + request.getOrderId() + " 处理过程中出现问题，订单已被取消。");
        }
    }

    private void notifyStore(String orderId, deliverystatus status) {
        // 由于具体 Store 服务的 API 未知，这里仅打印日志
        String message = "通知 Store：订单 " + orderId + " 状态更新为 " + status;
        logger.info(message);
        rabbitTemplate.convertAndSend("order.status.to.store", message);
    }

    private void sendEmail(String recipient, String subject, String message) {

        String emailApiUrl = emailServiceUrl + "/emails/send";

        // 创建邮件请求对象
        deliveryservice.mailrequest mailRequest = new deliveryservice.mailrequest();
        mailRequest.setRecipient(recipient);
        mailRequest.setSubject(subject);
        mailRequest.setMessage(message);

        // 发送 POST 请求到 Mail Service
        restTemplate.postForEntity(MAIL_SERVICE_URL, mailRequest, String.class);
    }

    // 定义一个简单的邮件请求类
    public static class mailrequest {
        private String recipient;
        private String subject;
        private String message;

        // Getters 和 Setters

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
