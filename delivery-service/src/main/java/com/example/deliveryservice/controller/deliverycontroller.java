package com.example.deliveryservice.controller;

import com.example.deliveryservice.model.deliveryrequest;
import com.example.deliveryservice.service.deliveryservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 送货控制器，用于接收送货请求
 */
@RestController
@RequestMapping("/deliveries")
public class deliverycontroller {

    @Autowired
    private deliveryservice deliveryService;

    @PostMapping("/create")
    public ResponseEntity<String> createDelivery(@RequestBody deliveryrequest request) {
        // 处理送货请求
        new Thread(() -> deliveryService.processDelivery(request)).start();
        return ResponseEntity.ok("送货请求已接收并处理中");
    }
}
