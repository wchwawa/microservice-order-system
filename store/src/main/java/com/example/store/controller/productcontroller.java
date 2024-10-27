package com.example.store.controller;

import com.example.store.entity.product;
import com.example.store.service.productservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/products")
public class productcontroller {
    private static final Logger logger = LoggerFactory.getLogger(productdisplaycontroller.class);
    @Autowired
    private productservice productService;
    @PostMapping()
    public ResponseEntity<product> createProduct(@RequestBody product product) {
        try {
            productService.createProduct(product);
            logger.info("Created product: {}", product.getName());
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error("Failed to create product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<product> updateProduct(@PathVariable Long id, @RequestBody product product) {
        try {
            product.setId(id);
            productService.updateProduct(product);
            logger.info("Updated product: {}", id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error("Failed to update product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            logger.info("Deleted product: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Failed to delete product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
