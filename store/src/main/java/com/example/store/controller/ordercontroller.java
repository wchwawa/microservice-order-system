package com.example.store.controller;

import com.example.store.service.orderservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ordercontroller {

    @Autowired
    private orderservice orderService;

    @PostMapping("/orders")
    public String createOrder(@RequestParam Long productId, @RequestParam int quantity, Model model, RedirectAttributes redirectAttributes) {
        try {
            String username = getCurrentUsername();
            orderService.createorder(username, productId, quantity);
            redirectAttributes.addFlashAttribute("message", "您的订单已成功创建！我们将尽快为您发货。");
            return "redirect:/ordersuccess";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/orderfailure";
        }
    }

    @GetMapping("/ordersuccess")
    public String showOrderSuccess() {
        return "ordersuccess";
    }

    @GetMapping("/orderfailure")
    public String showOrderFailure(Model model) {
        return "orderfailure";
    }

    // 获取当前用户名的辅助方法
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/orders")
    public String listOrders(Model model, Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("orders", orderService.getOrdersByUser(username));
        return "order_list";
    }

    @PostMapping("/orders/cancel")
    public String cancelOrder(@RequestParam Long orderId, RedirectAttributes redirectAttributes) {
        try {
            String username = getCurrentUsername();
            orderService.cancelOrder(username, orderId);
            redirectAttributes.addFlashAttribute("message", "您的订单已成功取消。");
            return "redirect:/ordercancelsuccess";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/ordercancelfailure";
        }
    }

    @GetMapping("/ordercancelsuccess")
    public String showOrderCancelSuccess() {
        return "ordercancelsuccess";
    }

    @GetMapping("/ordercancelfailure")
    public String showOrderCancelFailure() {
        return "ordercancelfailure";
    }
}
