package com.example.store.service;

import com.example.store.entity.inventory;
import com.example.store.mapper.inventorymapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class inventoryservice {

    @Autowired
    private inventorymapper inventoryMapper;

    public List<inventory> getAllInventory() {
        return inventoryMapper.findAllInventory();
    }
}