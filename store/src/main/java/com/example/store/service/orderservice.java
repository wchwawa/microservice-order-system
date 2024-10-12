package com.example.store.service;

import com.example.store.entity.*;
import com.example.store.mapper.*;
import com.example.store.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 移除自动装配的外部服务
    // @Autowired
    // private deliveryservice deliveryService;

    // @Autowired
    // private emailservice emailService;

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
    public void createorder(String username, Long productId, int quantity) {
        // 获取用户信息
        user user = userMapper.findByUsername(username);
        if (user == null) {
            logger.error("用户 {} 不存在", username);
            throw new RuntimeException("用户不存在");
        }

        // 获取商品信息
        product product = productMapper.findById(productId);
        if (product == null) {
            logger.error("商品 ID {} 不存在", productId);
            throw new RuntimeException("商品不存在");
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
            logger.warn("商品 ID {} 库存不足，需求量：{}，总可用库存：{}", productId, quantity, quantity - remainingQuantity);
            // 发送邮件通知
            try {
                Map<String, String> emailRequest = new HashMap<>();
                emailRequest.put("recipient", user.getEmail());
                emailRequest.put("subject", "订单失败");
                emailRequest.put("message", "抱歉，商品库存不足，无法完成您的订单。");

                String emailApiUrl = emailServiceUrl + "/emails/send";
                restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
                logger.debug("发送库存不足邮件给 {}", user.getEmail());
            } catch (Exception e) {
                logger.error("发送邮件失败：{}", e.getMessage());
            }
            throw new RuntimeException("库存不足");
        }

        // 更新库存
        for (Map.Entry<warehouse, Integer> entry : warehouseAllocation.entrySet()) {
            warehouse warehouse = entry.getKey();
            int allocatedQuantity = entry.getValue();
            inventory inventory = inventoryMapper.findByWarehouseAndProduct(warehouse.getId(), productId);
            inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);
            inventoryMapper.updateInventory(inventory);
            logger.debug("从仓库 {} 分配了 {} 个商品，剩余库存：{}", warehouse.getName(), allocatedQuantity, inventory.getQuantity());
        }

        // 计算总价
        double totalAmount = product.getPrice() * quantity;

        // 创建订单
        order order = new order();
        order.setUserId(user.getId());
        order.setTotalAmount(totalAmount);
        order.setStatus("待支付");
        orderMapper.insertOrder(order);
        logger.debug("创建订单 ID：{}", order.getId());

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
            logger.debug("创建订单项 ID：{}，仓库 ID：{}，数量：{}", item.getId(), warehouse.getId(), allocatedQuantity);
        }

        boolean paymentSuccess = processPayment(order, totalAmount);

        if (paymentSuccess) {
            // 更新订单状态
            orderMapper.updateOrderStatus(order.getId(), "已支付");
            logger.debug("订单 ID {} 已支付", order.getId());

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
                    logger.debug("发送仓库 {} 的送货请求，数量：{}", warehouse.getName(), allocatedQuantity);
                } catch (Exception e) {
                    logger.error("发送送货请求失败：{}", e.getMessage());
                }
            }

            // 发送邮件通知
            try {
                Map<String, String> emailRequest = new HashMap<>();
                emailRequest.put("recipient", user.getEmail());
                emailRequest.put("subject", "订单已支付");
                emailRequest.put("message", "您的订单已成功支付，我们将尽快为您发货。");

                String emailApiUrl = emailServiceUrl + "/emails/send";
                restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
                logger.debug("发送订单已支付邮件给 {}", user.getEmail());
            } catch (Exception e) {
                logger.error("发送邮件失败：{}", e.getMessage());
            }
        } else {
            // 支付失败，更新订单状态
            orderMapper.updateOrderStatus(order.getId(), "支付失败");
            logger.warn("订单 ID {} 支付失败", order.getId());

            // 恢复库存
            for (Map.Entry<warehouse, Integer> entry : warehouseAllocation.entrySet()) {
                warehouse warehouse = entry.getKey();
                int allocatedQuantity = entry.getValue();
                inventory inventory = inventoryMapper.findByWarehouseAndProduct(warehouse.getId(), productId);
                inventory.setQuantity(inventory.getQuantity() + allocatedQuantity);
                inventoryMapper.updateInventory(inventory);
                logger.debug("恢复仓库 {} 的库存 {} 个商品，当前库存：{}", warehouse.getName(), allocatedQuantity, inventory.getQuantity());
            }

            // 发送邮件通知
            try {
                Map<String, String> emailRequest = new HashMap<>();
                emailRequest.put("recipient", user.getEmail());
                emailRequest.put("subject", "订单支付失败");
                emailRequest.put("message", "您的订单支付失败，请稍后重试。");

                String emailApiUrl = emailServiceUrl + "/emails/send";
                restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
                logger.debug("发送订单支付失败邮件给 {}", user.getEmail());
            } catch (Exception e) {
                logger.error("发送邮件失败：{}", e.getMessage());
            }
            throw new RuntimeException("支付失败");
        }
    }

    private boolean processPayment(order order, double totalAmount) {
        // 创建支付请求
        paymentrequest paymentRequest = new paymentrequest();
        paymentRequest.setOrderId(order.getId().toString());
        paymentRequest.setAmount(BigDecimal.valueOf(totalAmount));
        paymentRequest.setCurrency("CNY");
        paymentRequest.setCustomerAccountId(CUSTOMER_ACCOUNT_ID);
        paymentRequest.setStoreAccountId(STORE_ACCOUNT_ID);

        try {
            // 调用银行服务的支付接口
            paymentresponse paymentResponse = restTemplate.postForObject(bankServiceUrl, paymentRequest, paymentresponse.class);

            if (paymentResponse != null && "SUCCESS".equals(paymentResponse.getStatus())) {
                logger.info("订单 ID {} 支付成功，交易 ID {}", order.getId(), paymentResponse.getTransactionId());
                return true;
            } else {
                logger.error("订单 ID {} 支付失败，原因：{}", order.getId(), paymentResponse != null ? paymentResponse.getMessage() : "未知错误");
                return false;
            }
        } catch (Exception e) {
            logger.error("订单 ID {} 支付异常，错误信息：{}", order.getId(), e.getMessage());
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
            logger.error("用户 {} 不存在，无法取消订单 {}", username, orderId);
            throw new RuntimeException("用户不存在");
        }

        order order = orderMapper.findById(orderId);
        if (order == null) {
            logger.error("订单 ID {} 不存在", orderId);
            throw new RuntimeException("订单不存在");
        }

        if (!order.getUserId().equals(user.getId())) {
            logger.warn("用户 {} 尝试取消非自己的订单 {}", username, orderId);
            throw new RuntimeException("无法取消非自己的订单");
        }

        if (!"已支付".equals(order.getStatus())) {
            logger.warn("订单 ID {} 状态为 {}，无法取消", orderId, order.getStatus());
            // 发送邮件通知
            try {
                Map<String, String> emailRequest = new HashMap<>();
                emailRequest.put("recipient", user.getEmail());
                emailRequest.put("subject", "订单无法取消");
                emailRequest.put("message", "抱歉，订单无法取消，请检查订单状态。");

                String emailApiUrl = emailServiceUrl + "/emails/send";
                restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
                logger.debug("发送订单无法取消邮件给 {}", user.getEmail());
            } catch (Exception e) {
                logger.error("发送邮件失败：{}", e.getMessage());
            }
            throw new RuntimeException("订单无法取消");
        }

        // 调用银行服务进行退款
        boolean refundSuccess = processRefund(order);

        if (refundSuccess) {
            // 更新订单状态
            orderMapper.updateOrderStatus(order.getId(), "已取消");
            logger.debug("订单 ID {} 已取消", order.getId());

            // 恢复库存
            List<orderitem> items = orderItemMapper.findByOrderId(order.getId());
            for (orderitem item : items) {
                Long warehouseId = item.getWarehouseId();
                inventory inventory = inventoryMapper.findByWarehouseAndProduct(warehouseId, item.getProductId());
                if (inventory != null) {
                    inventory.setQuantity(inventory.getQuantity() + item.getQuantity());
                    inventoryMapper.updateInventory(inventory);
                    logger.debug("恢复仓库 ID {} 的库存 {} 个商品，当前库存：{}", warehouseId, item.getQuantity(), inventory.getQuantity());
                }
            }

            // 发送邮件通知
            try {
                Map<String, String> emailRequest = new HashMap<>();
                emailRequest.put("recipient", user.getEmail());
                emailRequest.put("subject", "订单已取消");
                emailRequest.put("message", "您的订单已取消，退款将退回您的账户。");

                String emailApiUrl = emailServiceUrl + "/emails/send";
                restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
                logger.debug("发送订单已取消邮件给 {}", user.getEmail());
            } catch (Exception e) {
                logger.error("发送邮件失败：{}", e.getMessage());
            }
        } else {
            // 退款失败，处理异常
            logger.error("订单 ID {} 退款失败", orderId);
            // 发送邮件通知
            try {
                Map<String, String> emailRequest = new HashMap<>();
                emailRequest.put("recipient", user.getEmail());
                emailRequest.put("subject", "订单取消失败");
                emailRequest.put("message", "抱歉，订单取消失败，请联系客服。");

                String emailApiUrl = emailServiceUrl + "/emails/send";
                restTemplate.postForEntity(emailApiUrl, emailRequest, String.class);
                logger.debug("发送订单取消失败邮件给 {}", user.getEmail());
            } catch (Exception e) {
                logger.error("发送邮件失败：{}", e.getMessage());
            }
            throw new RuntimeException("退款失败");
        }
    }

    // 新增方法：调用银行服务进行退款
    private boolean processRefund(order order) {
        // 创建退款请求
        paymentrequest refundRequest = new paymentrequest();
        refundRequest.setOrderId(order.getId().toString());
        refundRequest.setAmount(BigDecimal.valueOf(order.getTotalAmount()));
        refundRequest.setCurrency("CNY");
        refundRequest.setCustomerAccountId(CUSTOMER_ACCOUNT_ID);
        refundRequest.setStoreAccountId(STORE_ACCOUNT_ID);

        try {
            // 调用银行服务的退款接口
            paymentresponse refundResponse = restTemplate.postForObject(bankServiceUrl + "/refund", refundRequest, paymentresponse.class);

            if (refundResponse != null && "SUCCESS".equals(refundResponse.getStatus())) {
                logger.info("订单 ID {} 退款成功，交易 ID {}", order.getId(), refundResponse.getTransactionId());
                return true;
            } else {
                logger.error("订单 ID {} 退款失败，原因：{}", order.getId(), refundResponse != null ? refundResponse.getMessage() : "未知错误");
                return false;
            }
        } catch (Exception e) {
            logger.error("订单 ID {} 退款异常，错误信息：{}", order.getId(), e.getMessage());
            return false;
        }
    }
}
