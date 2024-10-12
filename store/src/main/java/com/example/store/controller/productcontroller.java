package com.example.store.controller;

import com.example.store.entity.product;
import com.example.store.service.productservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class productcontroller {

    @Autowired
    private productservice productService;

    @GetMapping("/products")
    public String listProducts(Model model) {
        List<product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product_list";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product_detail";
    }
}
