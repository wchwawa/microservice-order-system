package com.example.store.model;

public class paymentresponse {
    private String status; // "SUCCESS" 或 "FAILURE"
    private String transactionId;
    private String message;

    // 无参构造函数
    public paymentresponse() {
    }

    // 带参数的构造函数
    public paymentresponse(String status, String transactionId, String message) {
        this.status = status;
        this.transactionId = transactionId;
        this.message = message;
    }

    // Getter 和 Setter 方法

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

