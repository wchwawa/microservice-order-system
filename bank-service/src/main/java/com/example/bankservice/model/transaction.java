package com.example.bankservice.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class transaction {
    private Long id;
    private String transactionId;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String customerAccountId;
    private String storeAccountId;
    private String type; // "PAYMENT" or "REFUND"
    private String status; // "SUCCESS" or "FAILURE"
    private Timestamp timestamp;

    // no args constructor
    public transaction() {
    }

    // all args constructor
    public transaction(Long id, String transactionId, String orderId, BigDecimal amount, String currency,
                       String customerAccountId, String storeAccountId, String type, String status, Timestamp timestamp) {
        this.id = id;
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.customerAccountId = customerAccountId;
        this.storeAccountId = storeAccountId;
        this.type = type;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getter and Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerAccountId() {
        return customerAccountId;
    }

    public void setCustomerAccountId(String customerAccountId) {
        this.customerAccountId = customerAccountId;
    }

    public String getStoreAccountId() {
        return storeAccountId;
    }

    public void setStoreAccountId(String storeAccountId) {
        this.storeAccountId = storeAccountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
