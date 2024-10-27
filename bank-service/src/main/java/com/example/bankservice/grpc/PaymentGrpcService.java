package com.example.bankservice.grpc;

import com.example.bankservice.service.paymentservice;
import com.example.bankservice.model.paymentrequest;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import java.math.BigDecimal;

@GrpcService
public class PaymentGrpcService extends PaymentServiceGrpc.PaymentServiceImplBase {
    
    private final paymentservice paymentService;

    public PaymentGrpcService(paymentservice paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void processPayment(PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        paymentrequest paymentRequest = new paymentrequest();
        paymentRequest.setOrderId(request.getOrderId());
        paymentRequest.setCustomerAccountId(request.getCustomerAccountId());
        paymentRequest.setStoreAccountId(request.getStoreAccountId());
        paymentRequest.setAmount(BigDecimal.valueOf(request.getAmount()));
        paymentRequest.setCurrency(request.getCurrency());
        
        com.example.bankservice.model.paymentresponse response = paymentService.processPayment(paymentRequest);
        
        PaymentResponse grpcResponse = PaymentResponse.newBuilder()
                .setStatus(response.getStatus())
                .setTransactionId(response.getTransactionId())
                .setMessage(response.getMessage())
                .build();
                
        responseObserver.onNext(grpcResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void processRefund(PaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        paymentrequest paymentRequest = new paymentrequest();
        paymentRequest.setOrderId(request.getOrderId());
        paymentRequest.setCustomerAccountId(request.getCustomerAccountId());
        paymentRequest.setStoreAccountId(request.getStoreAccountId());
        paymentRequest.setAmount(BigDecimal.valueOf(request.getAmount()));
        paymentRequest.setCurrency(request.getCurrency());

        com.example.bankservice.model.paymentresponse response = paymentService.processRefund(paymentRequest);

        PaymentResponse grpcResponse = PaymentResponse.newBuilder()
                .setStatus(response.getStatus())
                .setTransactionId(response.getTransactionId())
                .setMessage(response.getMessage())
                .build();

        responseObserver.onNext(grpcResponse);
        responseObserver.onCompleted();
    }
}