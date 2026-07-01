package com.jainbhavuk.razorpay.payment.gateway.adapter;

import com.jainbhavuk.razorpay.payment.gateway.dto.PaymentRequest;
import com.jainbhavuk.razorpay.payment.gateway.dto.PaymentResult;

public interface PaymentAdapter {
    public PaymentResult initiate(PaymentRequest paymentRequest);
}
