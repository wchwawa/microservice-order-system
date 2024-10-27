package com.example.store.controller;

import com.example.store.model.userdashboardinfo;
import com.example.store.service.userdashboardservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/dashboard")
public class dashboardcontroller {
    
    @Autowired
    private userdashboardservice dashboardService;
    
    @GetMapping
    public String showDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        userdashboardinfo dashboard = dashboardService.getUserDashboard(username);
        model.addAttribute("dashboard", dashboard);
        return "dashboard";
    }

    @PostMapping("/deposit")
    public String handleDeposit(
            @RequestParam BigDecimal amount,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        String username = authentication.getName();
        boolean success = dashboardService.deposit(username, amount);

        if (success) {
            redirectAttributes.addFlashAttribute("message", "Successfully deposited $" + amount);
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to deposit funds");
        }

        return "redirect:/dashboard";
    }
}