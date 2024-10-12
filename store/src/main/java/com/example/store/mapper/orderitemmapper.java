package com.example.store.mapper;

import com.example.store.entity.orderitem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface orderitemmapper {

    @Select("SELECT * FROM orderitem WHERE order_id = #{orderId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "product_id", property = "productId"),
            @Result(column = "quantity", property = "quantity"),
            @Result(column = "price", property = "price"),
            @Result(column = "warehouse_id", property = "warehouseId") // 映射 warehouse_id
    })
    List<orderitem> findByOrderId(@Param("orderId") Long orderId);

    @Insert("INSERT INTO orderitem (order_id, product_id, quantity, price, warehouse_id) VALUES (#{orderId}, #{productId}, #{quantity}, #{price}, #{warehouseId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertOrderItem(orderitem orderitem);
}
