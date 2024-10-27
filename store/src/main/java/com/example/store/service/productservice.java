package com.example.store.service;

import com.example.store.entity.product;
import com.example.store.mapper.productmapper;
import com.example.store.service.inventoryservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class productservice {

    private static final Logger logger = LoggerFactory.getLogger(productservice.class);

    @Autowired
    private productmapper productMapper;
    
    @Autowired
    private inventoryservice inventoryService;

    public List<product> getAllProducts() {
        return productMapper.findAll();
    }

    public product getProductById(Long id) {
        return productMapper.findById(id);
    }

    @Transactional
    public void createProduct(product product) {
        // 基本验证
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new RuntimeException("产品名称不能为空");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new RuntimeException("产品价格必须大于0");
        }

        try {
            productMapper.insertProduct(product);
            logger.info("Created product: {}, ID: {}", product.getName(), product.getId());
        } catch (Exception e) {
            logger.error("Failed to create product: {}", e.getMessage());
            throw new RuntimeException("创建产品失败");
        }
    }

    @Transactional
    public void updateProduct(product product) {
        // 基本验证
        if (product.getId() == null) {
            throw new RuntimeException("产品ID不能为空");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new RuntimeException("产品名称不能为空");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new RuntimeException("产品价格必须大于0");
        }

        try {
            product existing = productMapper.findById(product.getId());
            if (existing == null) {
                throw new RuntimeException("产品不存在");
            }
            productMapper.updateProduct(product);
            logger.info("Updated product: {}", product.getId());
        } catch (Exception e) {
            logger.error("Failed to update product: {}", e.getMessage());
            throw new RuntimeException("更新产品失败");
        }
    }

    @Transactional
    public void deleteProduct(Long id) {
        try {
            product existing = productMapper.findById(id);
            if (existing == null) {
                throw new RuntimeException("产品不存在");
            }

            productMapper.deleteProduct(id);
            logger.info("Deleted product: {}", id);
        } catch (Exception e) {
            logger.error("Failed to delete product: {}", e.getMessage());
            throw new RuntimeException("删除产品失败");
        }
    }
}
