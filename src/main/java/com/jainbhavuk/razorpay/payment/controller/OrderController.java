package com.jainbhavuk.razorpay.payment.controller;

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

    private UUID merchantId = UUID.fromString("e35feb4b-2c32-4c33-a8e0-eb94e145d68d");

    @PostMapping
    public ResponseEntity<OrderResponse> create (@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.create(merchantId, request));
    }
}
