package com.jainbhavuk.razorpay.payment.processor.dto;

import com.jainbhavuk.razorpay.common.entity.Money;
import com.jainbhavuk.razorpay.common.enums.PaymentMethod;

import java.util.Map;

public record PaymentProcessorRequest(
        PaymentMethod method,
        Money amount,
        Map<String, Object> methodDetails
) {
}
