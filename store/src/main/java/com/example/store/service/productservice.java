package com.example.store.service;

import com.example.store.entity.product;
import com.example.store.mapper.productmapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class productservice {

    @Autowired
    private productmapper productMapper;

    public List<product> getAllProducts() {
        return productMapper.findAll();
    }

    public product getProductById(Long id) {
        return productMapper.findById(id);
    }
}
