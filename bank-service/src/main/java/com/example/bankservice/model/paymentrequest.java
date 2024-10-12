package com.example.bankservice.model;

import java.math.BigDecimal;

public class paymentrequest {
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String customerAccountId;
    private String storeAccountId;

    // 无参构造函数
    public paymentrequest() {
    }

    // 带参数的构造函数
    public paymentrequest(String orderId, BigDecimal amount, String currency, String customerAccountId, String storeAccountId) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.customerAccountId = customerAccountId;
        this.storeAccountId = storeAccountId;
    }

    // Getter 和 Setter 方法

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
}
