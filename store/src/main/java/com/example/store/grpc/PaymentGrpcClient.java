package com.example.store.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class PaymentGrpcClient {
    
    @GrpcClient("bank-service")
    private PaymentServiceGrpc.PaymentServiceBlockingStub paymentStub;
    
    public com.example.store.model.paymentresponse processPayment(String orderId, 
            String customerAccountId, String storeAccountId, BigDecimal amount, String currency) {
        
        PaymentRequest request = PaymentRequest.newBuilder()
                .setOrderId(orderId)
                .setCustomerAccountId(customerAccountId)
                .setStoreAccountId(storeAccountId)
                .setAmount(amount.doubleValue())
                .setCurrency(currency)
                .build();
                
        PaymentResponse response = paymentStub.processPayment(request);
        
        return new com.example.store.model.paymentresponse(
                response.getStatus(),
                response.getTransactionId(),
                response.getMessage()
        );
    }
    
    public com.example.store.model.paymentresponse processRefund(String orderId, 
            String customerAccountId, String storeAccountId, BigDecimal amount, String currency) {
        
        PaymentRequest request = PaymentRequest.newBuilder()
                .setOrderId(orderId)
                .setCustomerAccountId(customerAccountId)
                .setStoreAccountId(storeAccountId)
                .setAmount(amount.doubleValue())
                .setCurrency(currency)
                .build();
                
        PaymentResponse response = paymentStub.processRefund(request);
        
        return new com.example.store.model.paymentresponse(
                response.getStatus(),
                response.getTransactionId(),
                response.getMessage()
        );
    }
}