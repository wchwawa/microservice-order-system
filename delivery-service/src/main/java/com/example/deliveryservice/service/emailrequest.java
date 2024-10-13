package com.example.deliveryservice.service;

public class emailrequest {
    private String recipient;
    private String subject;
    private String message;

    // 构造函数
    public emailrequest(String recipient, String subject, String message) {
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
    }

    // Getter 和 Setter 方法
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