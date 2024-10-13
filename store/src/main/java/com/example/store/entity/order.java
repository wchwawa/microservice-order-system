package com.example.store.entity;

import java.util.Date;
import java.util.List;

public class order {
    private Long id;
    private Long userId;
    private Double totalAmount;
    private String status;
    private Date createdAt;
    private List<orderitem> orderItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<orderitem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<orderitem> orderItems) {
        this.orderItems = orderItems;
    }
}
