package com.example.bankservice.controller;

import com.example.bankservice.model.paymentrequest;
import com.example.bankservice.model.paymentresponse;
import com.example.bankservice.service.paymentservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/payments")
public class paymentcontroller {

    @Autowired
    private paymentservice paymentService;

    @PostMapping
    public paymentresponse processPayment(@RequestBody paymentrequest paymentRequest) {
        return paymentService.processPayment(paymentRequest);
    }

    @PostMapping("/refund")
    public paymentresponse processRefund(@RequestBody paymentrequest paymentRequest) {
        return paymentService.processRefund(paymentRequest);
    }
}
