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
    }

    @Autowired
    private RestTemplate restTemplate;

    @Value("${email.service.url}")
    private String emailServiceUrl;

    public void sendEmail(String recipient, String subject, String message) {
        EmailRequest emailRequest = new EmailRequest(recipient, subject, message);

        String emailApiUrl = emailServiceUrl + "/emails/send";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
            logger.info("email sending is success: " + response.getBody());
        } catch (Exception e) {
            logger.error("email sending is failed: " + e.getMessage());
            throw new RuntimeException("can not send email");
        }
    }

    public static class EmailRequest {
        private String recipient;
        private String subject;
        private String message;

        public EmailRequest(String recipient, String subject, String message) {
            this.recipient = recipient;
            this.subject = subject;
            this.message = message;
        }

        // Getters and Setters

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
