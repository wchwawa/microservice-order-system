package com.example.store.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class deliveryservice {

    private Random random = new Random();

    @Async
    public void processDelivery(Long orderId, Long warehouseId, int quantity) {
        try {
            // 模拟取货
            Thread.sleep(5000);
            System.out.println("订单 " + orderId + " 已从仓库 " + warehouseId + " 取货");
            // 模拟运输途中
            Thread.sleep(5000);
            System.out.println("订单 " + orderId + " 运输中");
            // 模拟送达
            if (random.nextDouble() < 0.05) {
                // 模拟包裹丢失
                System.out.println("订单 " + orderId + " 包裹丢失");
                // 处理包裹丢失逻辑
            } else {
                System.out.println("订单 " + orderId + " 已送达");
                // 更新订单状态
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}