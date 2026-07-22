package com.jainbhavuk.razorpay.payment.service;

import com.jainbhavuk.razorpay.payment.dto.request.PaymentInitRequest;
import com.jainbhavuk.razorpay.payment.dto.response.PaymentResponse;
import org.springframework.http.HttpStatusCode;

import java.util.UUID;

public interface PaymentService {
    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);

    PaymentResponse capture(UUID merchantId, UUID paymentId);

    void resolveAuthorization(UUID id, boolean approved, String bankRef, String errorCode, String errorDescription);
}
