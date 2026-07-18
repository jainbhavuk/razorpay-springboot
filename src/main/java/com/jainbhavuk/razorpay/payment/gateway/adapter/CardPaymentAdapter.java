package com.jainbhavuk.razorpay.payment.gateway.adapter;

import com.jainbhavuk.razorpay.payment.gateway.dto.PaymentRequest;
import com.jainbhavuk.razorpay.payment.gateway.dto.PaymentResult;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CardPaymentAdapter implements PaymentAdapter {
    public PaymentResult initiate(PaymentRequest paymentRequest) {
    return null;
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}
