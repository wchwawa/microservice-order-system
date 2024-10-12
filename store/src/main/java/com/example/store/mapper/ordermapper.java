package com.example.store.mapper;

import com.example.store.entity.order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ordermapper {

    @Insert("INSERT INTO orders (user_id, total_amount, status) VALUES (#{userId}, #{totalAmount}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertOrder(order order);

    @Select("SELECT * FROM orders WHERE id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "total_amount", property = "totalAmount"),
            @Result(column = "status", property = "status")
    })
    order findById(@Param("id") Long id);

    @Select("SELECT * FROM orders WHERE user_id = #{userId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "total_amount", property = "totalAmount"),
            @Result(column = "status", property = "status")
    })
    List<order> findByUserId(@Param("userId") Long userId);

    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}")
    void updateOrderStatus(@Param("id") Long id, @Param("status") String status);
}
