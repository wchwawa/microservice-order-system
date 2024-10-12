package com.example.store.entity;

import java.util.Date;
import java.util.List;

public class user {
    private Long id;
    private String username;
    private String passwordHash;
    private String email;
    private Date createdAt;
    private List<orderitem> orderitems;

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<orderitem> getOrderitems() {
        return orderitems;
    }

    public void setOrderitems(List<orderitem> orderitems) {
        this.orderitems = orderitems;
    }
}
