package com.example.mailservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emails")
public class mailcontroller {

    private static final Logger logger = LoggerFactory.getLogger(mailcontroller.class);

    // 定义一个简单的邮件请求类
    public static class EmailRequest {
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

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        // 仅打印邮件信息
        logger.info("发送邮件给: {}", emailRequest.getRecipient());
        logger.info("邮件主题: {}", emailRequest.getSubject());
        logger.info("邮件内容: {}", emailRequest.getMessage());

        // 返回成功响应
        return ResponseEntity.ok("邮件已打印到控制台");
    }
}
