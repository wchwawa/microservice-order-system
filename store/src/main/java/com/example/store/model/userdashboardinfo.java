package com.example.store.model;

import java.math.BigDecimal;
import java.util.List;

public class userdashboardinfo {
    private BigDecimal accountBalance;
    private List<deliveryinfo> deliveries;

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public List<deliveryinfo> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<deliveryinfo> deliveries) {
        this.deliveries = deliveries;
    }
}