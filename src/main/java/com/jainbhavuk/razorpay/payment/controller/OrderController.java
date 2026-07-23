package com.jainbhavuk.razorpay.payment.controller;

import com.jainbhavuk.razorpay.merchant.security.MerchantContext;
import com.jainbhavuk.razorpay.payment.dto.request.CreateOrderRequest;
import com.jainbhavuk.razorpay.payment.dto.response.OrderResponse;
import com.jainbhavuk.razorpay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MerchantContext merchantContext;

    @PostMapping
    public ResponseEntity<OrderResponse> create (@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.create(merchantContext.getMerchantId(), request));
    }
}
