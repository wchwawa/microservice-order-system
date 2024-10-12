package com.example.bankservice.mapper;

import com.example.bankservice.model.transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface transactionmapper {

    @Insert("INSERT INTO transactions(transaction_id, order_id, amount, currency, customer_account_id, store_account_id, type, status) " +
            "VALUES(#{transactionId}, #{orderId}, #{amount}, #{currency}, #{customerAccountId}, #{storeAccountId}, #{type}, #{status})")
    void insertTransaction(transaction transaction);
}
