package com.example.store.service;

import org.springframework.stereotype.Service;

@Service
public class emailservice {

    public void sendEmail(String to, String subject, String content) {
        // 模拟发送邮件
        System.out.println("邮件已发送到 " + to + "，主题：" + subject);
        System.out.println("内容：" + content);
    }
}
