package com.example.bankservice.model;

import java.math.BigDecimal;

public class account {
    private Long id;
    private String accountId;
    private String username;
    private BigDecimal balance;

    // no args constructor
    public account() {
    }

    // all args constructor
    public account(Long id, String accountId, String username, BigDecimal balance) {
        this.id = id;
        this.accountId = accountId;
        this.username = username;
        this.balance = balance;
    }

    // Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
