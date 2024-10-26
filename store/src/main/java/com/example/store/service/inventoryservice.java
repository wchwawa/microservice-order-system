package com.example.store.service;

import com.example.store.entity.inventory;
import com.example.store.mapper.inventorymapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

@Service

public class inventoryservice {

    private static final Logger logger = LoggerFactory.getLogger(inventoryservice.class);
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();
    
    @Autowired
    private inventorymapper inventoryMapper;

    @Transactional(readOnly = true)
    public List<inventory> getAllInventory() {
        readLock.lock();
        try {
            return inventoryMapper.findAllInventory();
        } finally {
            readLock.unlock();
        }
    }

    @Transactional
    public void addInventory(inventory inventory) {
        writeLock.lock();
        try {
            inventory existing = inventoryMapper.findByWarehouseAndProductWithLock(
                    inventory.getWarehouseId(), 
                    inventory.getProductId()
            );
            
            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + inventory.getQuantity());
                inventoryMapper.updateInventory(existing);
                logger.info("Updated inventory quantity for warehouse {} and product {}", 
                    inventory.getWarehouseId(), inventory.getProductId());
            } else {
                inventoryMapper.insertInventory(inventory);
                logger.info("Created new inventory for warehouse {} and product {}", 
                    inventory.getWarehouseId(), inventory.getProductId());
            }
        } catch (Exception e) {
            logger.error("Failed to add inventory: {}", e.getMessage());
            throw new RuntimeException("库存添加失败", e);
        } finally {
            writeLock.unlock();
        }
    }

    @Transactional
    public void updateInventoryQuantity(Long warehouseId, Long productId, int quantityChange) {
        writeLock.lock();
        try {
            inventory inv = inventoryMapper.findByWarehouseAndProductWithLock(warehouseId, productId);
            if (inv == null) {
                throw new RuntimeException("库存记录不存在");
            }
            
            int newQuantity = inv.getQuantity() + quantityChange;
            if (newQuantity < 0) {
                throw new RuntimeException("库存不足");
            }
            
            inv.setQuantity(newQuantity);
            inventoryMapper.updateInventory(inv);
            logger.info("Updated inventory quantity: warehouse={}, product={}, change={}, new quantity={}", 
                warehouseId, productId, quantityChange, newQuantity);
        } catch (Exception e) {
            logger.error("Failed to update inventory quantity: {}", e.getMessage());
            throw new RuntimeException("库存更新失败", e);
        } finally {
            writeLock.unlock();
        }
    }

    public void updateInventory(inventory inventory) {
        inventoryMapper.updateInventory(inventory);
    }

    public void deleteInventory(Long id) {
        inventoryMapper.deleteInventory(id);
    }

    public inventory getInventoryById(Long id) {
        return inventoryMapper.findById(id);
    }
}
