package com.example.store.mapper;

import com.example.store.entity.product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface productmapper {

    @Select("SELECT * FROM products")
    List<product> findAll();

    @Select("SELECT * FROM products WHERE id = #{id}")
    product findById(@Param("id") Long id);

    @Insert("INSERT INTO products (name, description, price) VALUES (#{name}, #{description}, #{price})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertProduct(product product);

}
