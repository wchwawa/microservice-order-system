package com.example.store.model;

public class paymentresponse {
    private String status; // "SUCCESS" 或 "FAILURE"
    private String transactionId;
    private String message;

    public paymentresponse() {
    }

    public paymentresponse(String status, String transactionId, String message) {
        this.status = status;
        this.transactionId = transactionId;
        this.message = message;
    }


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

