package com.jainbhavuk.razorpay.payment.controller;

import com.jainbhavuk.razorpay.payment.dto.request.PaymentInitRequest;
import com.jainbhavuk.razorpay.payment.dto.response.PaymentResponse;
import com.jainbhavuk.razorpay.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final String merchantId = "ff777102-f2a1-4816-8689-52bedb9bac60";

    @PostMapping
    public ResponseEntity<PaymentResponse> initiatePayment(@Valid @RequestBody PaymentInitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiate(UUID.fromString(merchantId), request));
    }

    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponse> capture(@PathVariable UUID paymentId) {
     return ResponseEntity.ok(paymentService.capture(UUID.fromString(merchantId), paymentId));
    }
}
