package com.example.deliveryservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class emailservice {

    private static final Logger logger = LoggerFactory.getLogger(emailservice.class);

    public void sendEmail() {
        logger.info("Sending email...");
        // 其他代码
    }

    @Autowired
    private RestTemplate restTemplate;

    @Value("${email.service.url}")
    private String emailServiceUrl; // 确保变量名与 @Value 注解一致

    public void sendEmail(String recipient, String subject, String message) {
        // 构建请求体
        EmailRequest emailRequest = new EmailRequest(recipient, subject, message);

        // 调用 email-service 的 /emails/send 端点
        String emailApiUrl = emailServiceUrl + "/emails/send"; // 使用 emailServiceUrl
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
            logger.info("邮件发送成功: " + response.getBody());
        } catch (Exception e) {
            logger.error("邮件发送失败: " + e.getMessage());
            throw new RuntimeException("无法发送邮件");
        }
    }

    // 定义 EmailRequest 类
    public static class EmailRequest {
        private String recipient;
        private String subject;
        private String message;

        // 构造函数
        public EmailRequest(String recipient, String subject, String message) {
            this.recipient = recipient;
            this.subject = subject;
            this.message = message;
        }

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
