package com.example.store.mapper;

import com.example.store.entity.warehouse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface warehousemapper {

    @Select("SELECT * FROM warehouses")
    List<warehouse> findAll();

    @Select("SELECT * FROM warehouses WHERE id = #{id}")
    warehouse findById(@Param("id") Long id);

    @Insert("INSERT INTO warehouses (name, location) VALUES (#{name}, #{location})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertWarehouse(warehouse warehouse);

    @Update("UPDATE warehouses SET name = #{name}, location = #{location} WHERE id = #{id}")
    void updateWarehouse(warehouse warehouse);

    @Delete("DELETE FROM warehouses WHERE id = #{id}")
    void deleteWarehouse(@Param("id") Long id);
}
