package com.jainbhavuk.razorpay.payment.service.impl;

import com.jainbhavuk.razorpay.common.enums.OrderStatus;
import com.jainbhavuk.razorpay.common.exception.BusinessRuleViolationException;
import com.jainbhavuk.razorpay.common.exception.DuplicateResourceException;
import com.jainbhavuk.razorpay.common.exception.ResourceNotFoundException;
import com.jainbhavuk.razorpay.payment.dto.request.CreateOrderRequest;
import com.jainbhavuk.razorpay.payment.dto.response.OrderResponse;
import com.jainbhavuk.razorpay.payment.dto.response.PaymentResponse;
import com.jainbhavuk.razorpay.payment.entity.OrderRecord;
import com.jainbhavuk.razorpay.payment.entity.Payment;
import com.jainbhavuk.razorpay.payment.mapper.OrderMapper;
import com.jainbhavuk.razorpay.payment.mapper.PaymentMapper;
import com.jainbhavuk.razorpay.payment.repository.OrderRepository;
import com.jainbhavuk.razorpay.payment.repository.PaymentRepository;
import com.jainbhavuk.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final int defaultExpiryMinutes = 10;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {
        if(request.receipt() != null && orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())) {
            throw new DuplicateResourceException("Order with receipt " + request.receipt() + " already exists for merchant " + merchantId, "DUPLICATE_ORDER");
        }

        OrderRecord newOrder = OrderRecord.builder()
                .notes(request.notes())
                .receipt(request.receipt())
                .amount(request.amount())
                .merchantId(merchantId)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() : LocalDateTime.now().plusMinutes(defaultExpiryMinutes))
                .build();

        newOrder = orderRepository.save(newOrder);

        return orderMapper.toOrderResponse(newOrder);
    }

    @Override
    public OrderResponse getById(UUID orderId, UUID merchantId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId).orElseThrow(() -> new ResourceNotFoundException("ORDER", orderId));

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse cancel(UUID orderId, UUID merchantId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId).orElseThrow(() ->
                new ResourceNotFoundException("ORDER", orderId));

        if(order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.PAID) {
            throw new BusinessRuleViolationException("Cannot cancel order with status:" + order.getStatus(), "CANNOT_CANCEL_ORDER");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<PaymentResponse> listPayments(UUID orderId, UUID merchantId) {
        List<Payment> paymentList = paymentRepository.findByOrder_IdAndMerchantId(orderId, merchantId);

        return paymentMapper.toPaymentResponseList(paymentList);
    }
}
