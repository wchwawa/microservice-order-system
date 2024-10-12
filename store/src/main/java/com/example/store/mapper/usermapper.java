package com.example.store.mapper;

import com.example.store.entity.user;
import org.apache.ibatis.annotations.*;

@Mapper
public interface usermapper {

    @Select("SELECT * FROM users WHERE username = #{username}")
    user findByUsername(@Param("username") String username);

    @Insert("INSERT INTO users (username, password_hash, email) VALUES (#{username}, #{passwordHash}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(user user);

    // 其他方法（如更新用户信息等）
}
