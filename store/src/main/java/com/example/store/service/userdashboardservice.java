package com.example.store.service;

import com.example.store.entity.order;
import com.example.store.entity.user;
import com.example.store.mapper.ordermapper;
import com.example.store.mapper.usermapper;
import com.example.store.model.accountDTO;
import com.example.store.model.deliveryinfo;
import com.example.store.model.userdashboardinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
public class userdashboardservice {
    private static final Logger LOGGER = LoggerFactory.getLogger(userdashboardinfo.class);

    private final RestTemplate restTemplate;

    private final DeliveryStatusService deliveryStatusService;

    private final ordermapper orderMapper;

    private final usermapper userMapper;

    @Value("${bank.account.service.url}")
    private String bankServiceUrl;

    public userdashboardservice(RestTemplate restTemplate, DeliveryStatusService deliveryStatusService, ordermapper orderMapper, usermapper userMapper) {
        this.restTemplate = restTemplate;
        this.deliveryStatusService = deliveryStatusService;
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
    }

    public userdashboardinfo getUserDashboard(String username) {
        userdashboardinfo dashboard = new userdashboardinfo();

        // 先获取用户信息
        user user = userMapper.findByUsername(username);
        if (user == null) {
            LOGGER.error("User not found: {}", username);
            return dashboard;
        }

        String bankApiUrl = bankServiceUrl + "/user/" + username;
        try {
            ResponseEntity<accountDTO> accountResponse = restTemplate.getForEntity(
                    bankApiUrl,
                    accountDTO.class
            );
            if (accountResponse.getStatusCode() == HttpStatus.OK) {
                dashboard.setAccountBalance(Objects.requireNonNull(accountResponse.getBody()).getBalance());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to fetch account information: {}", e.getMessage());
        }

        // 获取用户的订单配送信息
        List<deliveryinfo> deliveries = getRecentDeliveries(user.getId());
        dashboard.setDeliveries(deliveries);

        return dashboard;
    }

    public boolean deposit(String username, BigDecimal amount) {
        try {
            String depositUrl = bankServiceUrl + "/" + username;
            Map<String, BigDecimal> request = new HashMap<>();
            request.put("amount", amount);

            ResponseEntity<accountDTO> response = restTemplate.postForEntity(
                    depositUrl,
                    request,
                    accountDTO.class
            );

            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            LOGGER.error("Failed to deposit: {}", e.getMessage());
            return false;
        }
    }

    private List<deliveryinfo> getRecentDeliveries(Long userId) {
        try {
            List<order> userOrders = orderMapper.findByUserId(userId);
            if (userOrders.isEmpty()) {
                return new ArrayList<>();
            }

            List<String> orderIds = userOrders.stream()
                    .map(order -> order.getId().toString())
                    .toList();

            return deliveryStatusService.getDeliveryStatusByOrderIds(orderIds);
        } catch (Exception e) {
            LOGGER.error("Failed to fetch delivery information: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}