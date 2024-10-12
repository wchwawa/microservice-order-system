package com.example.deliveryservice.model;

public enum deliverystatus {
    RECEIVED,       // 送货请求已接收
    PICKED_UP,      // 已从仓库取货
    IN_TRANSIT,     // 运输中
    DELIVERED,      // 已送达
    FAILED          // 处理失败
}
