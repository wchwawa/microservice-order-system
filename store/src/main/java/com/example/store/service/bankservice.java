package com.example.store.service;

import org.springframework.stereotype.Service;

@Service
public class bankservice {

    public boolean transfer(String fromAccount, String toAccount, double amount) {
        // 模拟银行转账逻辑
        // 这里直接返回 true 表示成功
        return true;
    }

    public boolean refund(String fromAccount, String toAccount, double amount) {
        // 模拟退款逻辑
        return true;
    }
}
