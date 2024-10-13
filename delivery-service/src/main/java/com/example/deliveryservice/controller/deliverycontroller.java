package com.example.deliveryservice.controller;

import com.example.deliveryservice.model.deliveryrequest;
import com.example.deliveryservice.service.deliveryservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveries")
public class deliverycontroller {

    @Autowired
    private deliveryservice deliveryService;

    @PostMapping("/create")
    public ResponseEntity<String> createDelivery(@RequestBody deliveryrequest request) {
        // handle the request asynchronously
        new Thread(() -> deliveryService.processDelivery(request)).start();
        return ResponseEntity.ok("delivery request received and being processed");
    }
}
