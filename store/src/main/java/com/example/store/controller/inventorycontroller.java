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
        return "inventory"; // returns the inventory.html template
    }
}
