package com.example.bankservice.service;

import com.example.bankservice.mapper.accountmapper;
import com.example.bankservice.mapper.transactionmapper;
import com.example.bankservice.model.account;
import com.example.bankservice.model.paymentrequest;
import com.example.bankservice.model.paymentresponse;
import com.example.bankservice.model.transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class paymentservice {

    @Autowired
    private accountmapper accountMapper;

    @Autowired
    private transactionmapper transactionMapper;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public paymentresponse processPayment(paymentrequest request) {
        String transactionId = UUID.randomUUID().toString();
        String status;
        String message;

        try {

            account customerAccount = accountMapper.findAccountByAccountIdWithLock(request.getCustomerAccountId());
            account storeAccount = accountMapper.findAccountByAccountIdWithLock(request.getStoreAccountId());

            if (customerAccount == null) {
                status = "FAILURE";
                message = "customer account does not exist";
            } else if (storeAccount == null) {
                status = "FAILURE";
                message = "store account does not exist";
            } else {
                BigDecimal amount = request.getAmount();
                BigDecimal customerBalance = customerAccount.getBalance();

                if (customerBalance.compareTo(amount) >= 0) {
                    // 客户账户扣款
                    customerAccount.setBalance(customerBalance.subtract(amount));
                    accountMapper.updateAccountBalance(customerAccount.getAccountId(), customerAccount.getBalance());

                    // 商店账户加款
                    BigDecimal storeBalance = storeAccount.getBalance().add(amount);
                    storeAccount.setBalance(storeBalance);
                    accountMapper.updateAccountBalance(storeAccount.getAccountId(), storeAccount.getBalance());

                    status = "SUCCESS";
                    message = "payment successful";
                } else {
                    status = "FAILURE";
                    message = "customer account balance is insufficient";
                }
            }

            // 创建交易记录
            transaction transaction = new transaction();
            transaction.setTransactionId(transactionId);
            transaction.setOrderId(request.getOrderId());
            transaction.setAmount(request.getAmount());
            transaction.setCurrency(request.getCurrency());
            transaction.setCustomerAccountId(request.getCustomerAccountId());
            transaction.setStoreAccountId(request.getStoreAccountId());
            transaction.setType("PAYMENT");
            transaction.setStatus(status);

            transactionMapper.insertTransaction(transaction);

            // 返回响应
            return new paymentresponse(status, transactionId, message);

        } catch (Exception e) {
            return new paymentresponse("FAILURE", transactionId, "交易处理失败: " + e.getMessage());
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public paymentresponse processRefund(paymentrequest request) {
        String transactionId = UUID.randomUUID().toString();
        String status;
        String message;

        try {
            // 获取客户账户和商店账户（带锁）
            account storeAccount = accountMapper.findAccountByAccountIdWithLock(request.getStoreAccountId());
            account customerAccount = accountMapper.findAccountByAccountIdWithLock(request.getCustomerAccountId());

            if (customerAccount == null) {
                status = "FAILURE";
                message = "客户账户不存在";
            } else if (storeAccount == null) {
                status = "FAILURE";
                message = "商店账户不存在";
            } else {
                BigDecimal amount = request.getAmount();
                BigDecimal storeBalance = storeAccount.getBalance();

                if (storeBalance.compareTo(amount) >= 0) {
                    // 商店账户扣款
                    storeAccount.setBalance(storeBalance.subtract(amount));
                    accountMapper.updateAccountBalance(storeAccount.getAccountId(), storeAccount.getBalance());

                    // 客户账户加款
                    BigDecimal customerBalance = customerAccount.getBalance().add(amount);
                    customerAccount.setBalance(customerBalance);
                    accountMapper.updateAccountBalance(customerAccount.getAccountId(), customerAccount.getBalance());

                    status = "SUCCESS";
                    message = "退款成功";
                } else {
                    status = "FAILURE";
                    message = "商店账户余额不足，无法退款";
                }
            }

            // 创建交易记录
            transaction transaction = new transaction();
            transaction.setTransactionId(transactionId);
            transaction.setOrderId(request.getOrderId());
            transaction.setAmount(request.getAmount());
            transaction.setCurrency(request.getCurrency());
            transaction.setCustomerAccountId(request.getCustomerAccountId());
            transaction.setStoreAccountId(request.getStoreAccountId());
            transaction.setType("REFUND");
            transaction.setStatus(status);

            transactionMapper.insertTransaction(transaction);

            return new paymentresponse(status, transactionId, message);

        } catch (Exception e) {
            return new paymentresponse("FAILURE", transactionId, "退款处理失败: " + e.getMessage());
        }
    }
}
