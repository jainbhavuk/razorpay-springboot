package com.jainbhavuk.razorpay.payment.service;

import com.jainbhavuk.razorpay.payment.dto.response.PaymentResponse;
import com.jainbhavuk.razorpay.payment.dto.request.CreateOrderRequest;
import com.jainbhavuk.razorpay.payment.dto.response.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse create(UUID merchantId, CreateOrderRequest request);
    OrderResponse getById(UUID orderId, UUID merchantId);
    OrderResponse cancel(UUID orderId, UUID merchantId);
    List<PaymentResponse> listPayments(UUID orderId, UUID merchantId);
}
