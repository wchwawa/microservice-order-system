package com.example.bankservice.model;

public class paymentresponse {
    private String status; // "SUCCESS" 或 "FAILURE"
    private String transactionId;
    private String message;

    // no args constructor
    public paymentresponse() {
    }

    // all args constructor
    public paymentresponse(String status, String transactionId, String message) {
        this.status = status;
        this.transactionId = transactionId;
        this.message = message;
    }

    // Getter and Setter

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
