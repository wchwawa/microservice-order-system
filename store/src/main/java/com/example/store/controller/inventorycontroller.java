package com.example.store.controller;

import com.example.store.entity.inventory;
import com.example.store.service.inventoryservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")  // 修改为 /api 前缀
public class inventorycontroller {

    @Autowired
    private inventoryservice inventoryService;

    @GetMapping("/list")
    public ResponseEntity<List<inventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addInventory(@RequestBody inventory inventory) {
        try {
            if (inventory.getWarehouseId() == null || 
                inventory.getProductId() == null || 
                inventory.getQuantity() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "仓库ID、产品ID和数量不能为空"));
            }
            
            inventoryService.addInventory(inventory);
            return ResponseEntity.ok(Map.of("message", "库存添加成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updateInventory(@PathVariable Long id, @RequestBody inventory inventory) {
        try {
            inventory.setId(id);
            inventoryService.updateInventory(inventory);
            return ResponseEntity.ok(Map.of("message", "库存更新成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable Long id) {
        try {
            inventoryService.deleteInventory(id);
            return ResponseEntity.ok(Map.of("message", "库存删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
