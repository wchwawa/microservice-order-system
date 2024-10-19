package com.example.store.service;

import com.example.store.entity.*;
import com.example.store.mapper.*;
import com.example.store.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.*;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 导入所需的类
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

@Service
public class orderservice {

    private static final Logger logger = LoggerFactory.getLogger(orderservice.class);

    @Autowired
    private usermapper userMapper;

    @Autowired
    private productmapper productMapper;

    @Autowired
    private ordermapper orderMapper;

    @Autowired
    private orderitemmapper orderItemMapper;

    @Autowired
    private inventorymapper inventoryMapper;

    @Autowired
    private warehousemapper warehouseMapper;

    // 添加 RestTemplate，用于调用外部服务
    @Autowired
    private RestTemplate restTemplate;

    // 从配置文件中获取外部服务的 URL
    @Value("${bank.service.url}")
    private String bankServiceUrl;

    @Value("${delivery.service.url}")
    private String deliveryServiceUrl;

    @Value("${email.service.url}")
    private String emailServiceUrl;

    // 定义客户和商店的账户 ID
    private static final String CUSTOMER_ACCOUNT_ID = "customer_account_001";
    private static final String STORE_ACCOUNT_ID = "store_account_001";

    @Transactional
    public void createorder(String username, Long productId, int quantity) throws InterruptedException {
        // 获取用户信息
        user user = userMapper.findByUsername(username);
        if (user == null) {
            logger.error("user {} not exist", username);
            throw new RuntimeException("user not exist");
        }

        // 获取商品信息
        product product = productMapper.findById(productId);
        if (product == null) {
            logger.error("Item ID {} not exist", productId);
            throw new RuntimeException("Item not exist");
        }

        // 查找所有仓库的库存
        List<warehouse> warehouses = warehouseMapper.findAll();
        Map<warehouse, Integer> warehouseAllocation = new LinkedHashMap<>();
        int remainingQuantity = quantity;

        // 遍历仓库，分配库存
        for (warehouse warehouse : warehouses) {
            inventory inventory = inventoryMapper.findByWarehouseAndProduct(warehouse.getId(), productId);
            if (inventory != null && inventory.getQuantity() > 0) {
                int availableQuantity = inventory.getQuantity();
                if (availableQuantity >= remainingQuantity) {
                    warehouseAllocation.put(warehouse, remainingQuantity);
                    remainingQuantity = 0;
                    break;
                } else {
                    warehouseAllocation.put(warehouse, availableQuantity);
                    remainingQuantity -= availableQuantity;
                }
            }
        }

        if (remainingQuantity > 0) {
            // 总库存不足，处理缺货情况
            logger.warn("Product ID {} is out of stock, requested quantity: {}, total available stock: {}", productId, quantity, quantity - remainingQuantity);
            // 发送邮件通知
            try {
                Map<String, String> emailRequest = new HashMap<>();
                emailRequest.put("recipient", user.getEmail());
                emailRequest.put("subject", "Order Failed");
                emailRequest.put("message", "Sorry, the product is out of stock and we cannot complete your order.");

                String emailApiUrl = emailServiceUrl + "/emails/send";
                restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
                logger.debug("Sent out-of-stock email to {}", user.getEmail());
            } catch (Exception e) {
                logger.error("failed to sending order：{}", e.getMessage());
            }
            throw new RuntimeException("Out of stock");
        }

        // 更新库存
        for (Map.Entry<warehouse, Integer> entry : warehouseAllocation.entrySet()) {
            warehouse warehouse = entry.getKey();
            int allocatedQuantity = entry.getValue();
            inventory inventory = inventoryMapper.findByWarehouseAndProduct(warehouse.getId(), productId);
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            inventoryMapper.updateInventory(inventory);
            logger.debug("Allocated {} items from warehouse {}, remaining stock: {}", allocatedQuantity, warehouse.getName(), inventory.getQuantity());
        }

        // 计算总价
        double totalAmount = product.getPrice() * quantity;

        // 创建订单
        order order = new order();
        order.setUserId(user.getId());
        order.setTotalAmount(totalAmount);
        order.setStatus("waiting for payment");
        orderMapper.insertOrder(order);
        logger.debug("Create order. ID：{}", order.getId());

        for (Map.Entry<warehouse, Integer> entry : warehouseAllocation.entrySet()) {
            warehouse warehouse = entry.getKey();
            int allocatedQuantity = entry.getValue();
            orderitem item = new orderitem();
            item.setOrderId(order.getId());
            item.setProductId(productId);
            item.setQuantity(allocatedQuantity);
            item.setPrice(product.getPrice());
            item.setWarehouseId(warehouse.getId());
            orderItemMapper.insertOrderItem(item);
            logger.debug("Create order ID：{}，warehouse ID：{}，quantity：{}", item.getId(), warehouse.getId(), allocatedQuantity);
        }
        //mocking order placed successfully but payment not done yet context
        Thread.sleep(2000);

        boolean paymentSuccess = processPayment(order, totalAmount);

        // payment success
        if (paymentSuccess) {
            // 更新订单状态
            orderMapper.updateOrderStatus(order.getId(), "paid");
            logger.debug("order ID {} has been paid", order.getId());

            // 发送送货请求
            for (Map.Entry<warehouse, Integer> entry : warehouseAllocation.entrySet()) {
                warehouse warehouse = entry.getKey();
                int allocatedQuantity = entry.getValue();
                try {
                    Map<String, Object> deliveryRequest = new HashMap<>();
                    deliveryRequest.put("orderId", order.getId().toString());
                    deliveryRequest.put("customerEmail", user.getEmail());
                    deliveryRequest.put("warehouseName", warehouse.getName());
                    deliveryRequest.put("productName", product.getName());
                    deliveryRequest.put("quantity", allocatedQuantity);

                    String deliveryApiUrl = deliveryServiceUrl + "/deliveries/create";
                    restTemplate.postForEntity(deliveryApiUrl, deliveryRequest, String.class);
                    logger.debug("sending delivery request to warehouse {}，quantity：{}", warehouse.getName(), allocatedQuantity);
                } catch (Exception e) {
                    logger.error("delivery request sending failed：{}", e.getMessage());
                }
            }

            // 发送邮件通知
            try {
                Map<String, String> emailRequest = new HashMap<>();
                emailRequest.put("recipient", user.getEmail());
                emailRequest.put("subject", "order paid");
                emailRequest.put("message", "your order has been paid successfully, we will deliver it soon.");

                String emailApiUrl = emailServiceUrl + "/emails/send";
                restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
                logger.debug("sending order has been paid email to {}", user.getEmail());
            } catch (Exception e) {
                logger.error("fail to send email：{}", e.getMessage());
            }
        } else {
            // 支付失败，更新订单状态
            orderMapper.updateOrderStatus(order.getId(), "fail to pay");
            logger.warn("order ID {} payment failed", order.getId());

            // 恢复库存
            for (Map.Entry<warehouse, Integer> entry : warehouseAllocation.entrySet()) {
                warehouse warehouse = entry.getKey();
                int allocatedQuantity = entry.getValue();
                inventory inventory = inventoryMapper.findByWarehouseAndProduct(warehouse.getId(), productId);
                inventory.setQuantity(inventory.getQuantity() + allocatedQuantity);
                inventoryMapper.updateInventory(inventory);
                logger.debug("restore warehouse {} {} items，current inventory：{}", warehouse.getName(), allocatedQuantity, inventory.getQuantity());
            }

            // 发送邮件通知
            try {
                Map<String, String> emailRequest = new HashMap<>();
                emailRequest.put("recipient", user.getEmail());
                emailRequest.put("subject", "paid failed");
                emailRequest.put("message", "sorry, your order payment failed, please try again.");

                String emailApiUrl = emailServiceUrl + "/emails/send";
                restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
                logger.debug("failed to send order-failed email {}", user.getEmail());
            } catch (Exception e) {
                logger.error("failed to send eamil：{}", e.getMessage());
            }
            throw new RuntimeException("payment failed");
        }
    }

    private boolean processPayment(order order, double totalAmount) {
        // Create payment request
        paymentrequest paymentRequest = new paymentrequest();
        paymentRequest.setOrderId(order.getId().toString());
        paymentRequest.setAmount(BigDecimal.valueOf(totalAmount));
        paymentRequest.setCurrency("CNY");
        paymentRequest.setCustomerAccountId(CUSTOMER_ACCOUNT_ID);
        paymentRequest.setStoreAccountId(STORE_ACCOUNT_ID);

        try {
            // Call bank service payment interface
            paymentresponse paymentResponse = restTemplate.postForObject(bankServiceUrl, paymentRequest, paymentresponse.class);

            if (paymentResponse != null && "SUCCESS".equals(paymentResponse.getStatus())) {
                logger.info("Order ID {} payment successful, transaction ID {}", order.getId(), paymentResponse.getTransactionId());
                return true;
            } else {
                logger.error("Order ID {} payment failed, reason: {}", order.getId(), paymentResponse != null ? paymentResponse.getMessage() : "Unknown error");
                return false;
            }
        } catch (Exception e) {
            logger.error("Order ID {} payment exception, error message: {}", order.getId(), e.getMessage());
            return false;
        }
    }

    public List<order> getOrdersByUser(String username) {
        user user = userMapper.findByUsername(username);
        if (user == null) {
            return Collections.emptyList();
        }
        return orderMapper.findByUserId(user.getId());
    }

    @Transactional
    public void cancelOrder(String username, Long orderId) {
        user user = userMapper.findByUsername(username);
        if (user == null) {
            logger.error("User {} does not exist, cannot cancel order {}", username, orderId);
            throw new RuntimeException("User does not exist");
        }

        order order = orderMapper.findById(orderId);
        if (order == null) {
            logger.error("Order ID {} does not exist", orderId);
            throw new RuntimeException("Order does not exist");
        }

        if (!order.getUserId().equals(user.getId())) {
            logger.warn("User {} attempted to cancel order {} that does not belong to them", username, orderId);
            throw new RuntimeException("Cannot cancel an order that does not belong to you");
        }

        if (!"paid".equals(order.getStatus())) {
            logger.warn("Order ID {} status is {}, cannot be cancelled", orderId, order.getStatus());
            // Send email notification
            try {
                Map<String, String> emailRequest = new HashMap<>();
                emailRequest.put("recipient", user.getEmail());
                emailRequest.put("subject", "Order Cannot Be Cancelled");
                emailRequest.put("message", "Sorry, the order cannot be cancelled. Please check the order status.");

                String emailApiUrl = emailServiceUrl + "/emails/send";
                restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
                logger.debug("Sent order cannot be cancelled email to {}", user.getEmail());
            } catch (Exception e) {
                logger.error("Failed to send email: {}", e.getMessage());
            }
            throw new RuntimeException("Order cannot be cancelled");
        }

        // Call bank service for refund
        boolean refundSuccess = processRefund(order);

        if (refundSuccess) {
            // Update order status
            orderMapper.updateOrderStatus(order.getId(), "Cancelled");
            logger.debug("Order ID {} has been cancelled", order.getId());

            // Restore inventory
            List<orderitem> items = orderItemMapper.findByOrderId(order.getId());
            for (orderitem item : items) {
                Long warehouseId = item.getWarehouseId();
                inventory inventory = inventoryMapper.findByWarehouseAndProduct(warehouseId, item.getProductId());
                if (inventory != null) {
                    inventory.setQuantity(inventory.getQuantity() + item.getQuantity());
                    inventoryMapper.updateInventory(inventory);
                    logger.debug("Restored {} items to warehouse ID {}, current inventory: {}", item.getQuantity(), warehouseId, inventory.getQuantity());
                }
            }

            // Send email notification
            try {
                Map<String, String> emailRequest = new HashMap<>();
                emailRequest.put("recipient", user.getEmail());
                emailRequest.put("subject", "Order Cancelled");
                emailRequest.put("message", "Your order has been cancelled. The refund will be returned to your account.");

                String emailApiUrl = emailServiceUrl + "/emails/send";
                restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
                logger.debug("Sent order cancelled email to {}", user.getEmail());
            } catch (Exception e) {
                logger.error("Failed to send email: {}", e.getMessage());
            }
        } else {
            // Refund failed, handle exception
            logger.error("Order ID {} refund failed", orderId);
            // Send email notification
            try {
                Map<String, String> emailRequest = new HashMap<>();
                emailRequest.put("recipient", user.getEmail());
                emailRequest.put("subject", "Order Cancellation Failed");
                emailRequest.put("message", "Sorry, order cancellation failed. Please contact customer service.");

                String emailApiUrl = emailServiceUrl + "/emails/send";
                restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
                logger.debug("Sent order cancellation failed email to {}", user.getEmail());
            } catch (Exception e) {
                logger.error("Failed to send email: {}", e.getMessage());
            }
            throw new RuntimeException("Refund failed");
        }
    }

    // New method: Call bank service for refund
    private boolean processRefund(order order) {
        paymentrequest refundRequest = new paymentrequest();
        refundRequest.setOrderId(order.getId().toString());
        refundRequest.setAmount(BigDecimal.valueOf(order.getTotalAmount()));
        refundRequest.setCurrency("CNY");
        refundRequest.setCustomerAccountId(CUSTOMER_ACCOUNT_ID);
        refundRequest.setStoreAccountId(STORE_ACCOUNT_ID);

        try {
            // refund api
            paymentresponse refundResponse = restTemplate.postForObject(bankServiceUrl + "/refund", refundRequest, paymentresponse.class);

            if (refundResponse != null && "SUCCESS".equals(refundResponse.getStatus())) {
                logger.info("Order ID {} refund successful, transaction ID {}", order.getId(), refundResponse.getTransactionId());
                return true;
            } else {
                logger.error("Order ID {} refund failed, reason: {}", order.getId(), refundResponse != null ? refundResponse.getMessage() : "Unknown error");
                return false;
            }
        } catch (Exception e) {
            logger.error("Order ID {} refund exception, error message: {}", order.getId(), e.getMessage());
            return false;
        }
    }
}
