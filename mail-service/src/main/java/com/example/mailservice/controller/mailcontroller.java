package com.example.mailservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emails")
public class mailcontroller {

    private static final Logger logger = LoggerFactory.getLogger(mailcontroller.class);

    public static class EmailRequest {
        private String recipient;
        private String subject;
        private String message;


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

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        // 仅打印邮件信息
        logger.info("sending email to: {}", emailRequest.getRecipient());
        logger.info("title: {}", emailRequest.getSubject());
        logger.info("content: {}", emailRequest.getMessage());

        // 返回成功响应
        return ResponseEntity.ok("email sent successfully");
    }
}
