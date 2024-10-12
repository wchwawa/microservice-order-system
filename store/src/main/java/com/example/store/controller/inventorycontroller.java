package com.example.store.controller;

import com.example.store.entity.inventory;
import com.example.store.service.inventoryservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class inventorycontroller {

    @Autowired
    private inventoryservice inventoryService;

    @GetMapping("/inventory")
    public String showInventory(Model model) {
        List<inventory> inventoryList = inventoryService.getAllInventory();
        model.addAttribute("inventoryList", inventoryList);
        return "inventory"; // 返回的视图名称，将在后续步骤中创建对应的 HTML 文件
    }
}
