package com.example.store.mapper;

import com.example.store.entity.inventory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface inventorymapper {

    @Select("SELECT * FROM inventory")
    List<inventory> findAll();

    @Select("SELECT * FROM inventory WHERE id = #{id}")
    inventory findById(@Param("id") Long id);

    @Insert("INSERT INTO inventory (warehouse_id, product_id, quantity) VALUES (#{warehouseId}, #{productId}, #{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertInventory(inventory inventory);

    @Delete("DELETE FROM inventory WHERE id = #{id}")
    void deleteInventory(@Param("id") Long id);

    @Select("SELECT * FROM inventory WHERE warehouse_id = #{warehouseId} AND product_id = #{productId} FOR UPDATE")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "warehouse_id", property = "warehouseId"),
            @Result(column = "product_id", property = "productId"),
            @Result(column = "quantity", property = "quantity")
    })
    inventory findByWarehouseAndProductWithLock(@Param("warehouseId") Long warehouseId, @Param("productId") Long productId);

    @Update("UPDATE inventory SET quantity = #{quantity} WHERE warehouse_id = #{warehouseId} AND product_id = #{productId}")
    void updateInventory(inventory inventory);

    @Select("SELECT i.*, w.name AS warehouse_name, p.name AS product_name " +
            "FROM inventory i " +
            "JOIN warehouses w ON i.warehouse_id = w.id " +
            "JOIN products p ON i.product_id = p.id")
    List<inventory> findAllInventory();
}
