package com.example.bankservice.mapper;

import com.example.bankservice.model.account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface accountmapper {

    @Select("SELECT * FROM accounts WHERE account_id = #{accountId}")
    account findAccountByAccountId(@Param("accountId") String accountId);

    // 添加带锁的查询方法
    @Select("SELECT * FROM accounts WHERE account_id = #{accountId} FOR UPDATE")
    account findAccountByAccountIdWithLock(@Param("accountId") String accountId);

    @Update("UPDATE accounts SET balance = #{balance} WHERE account_id = #{accountId}")
    void updateAccountBalance(@Param("accountId") String accountId, @Param("balance") java.math.BigDecimal balance);

    @Select("SELECT * FROM accounts WHERE username = #{username}")
    account findAccountByUsername(@Param("username") String username);
}
