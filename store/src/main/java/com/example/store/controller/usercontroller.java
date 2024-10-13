package com.example.store.controller;

import com.example.store.entity.user;
import com.example.store.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class usercontroller {

    @Autowired
    private userservice userservice;


    @PostMapping("/user/register")
    public String registerUser(@ModelAttribute user user, Model model) {
        if (userservice.isUsernameExists(user.getUsername())) {
            model.addAttribute("usernameExists", true);
            return "register";
        }
        userservice.register(user);
        return "redirect:/login?registerSuccess";
    }
}
