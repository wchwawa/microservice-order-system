package com.example.store.service;

import com.example.store.model.deliveryinfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DeliveryStatusService {
    private final Map<String, deliveryinfo> deliveryStatuses = new ConcurrentHashMap<>();

    public void updateDeliveryStatus(String orderId, String status) {
        deliveryinfo info = new deliveryinfo();
        info.setOrderId(orderId);
        info.setStatus(status);
        info.setUpdateTime(LocalDateTime.now());
        deliveryStatuses.put(orderId, info);
    }

    public List<deliveryinfo> getDeliveryStatusByOrderIds(List<String> orderIds) {
        List<deliveryinfo> result = new ArrayList<>();
        for (String orderId : orderIds) {
            deliveryinfo status = deliveryStatuses.get(orderId);
            if (status != null) {
                result.add(status);
            }
        }
        return result;
    }
}