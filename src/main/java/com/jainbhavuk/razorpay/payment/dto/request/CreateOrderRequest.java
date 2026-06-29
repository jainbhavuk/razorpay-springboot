package com.jainbhavuk.razorpay.payment.dto.request;

import com.jainbhavuk.razorpay.common.entity.Money;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

public record CreateOrderRequest(
        @NotNull
        Money amount,
        String receipt,
        Map<String, Object> notes,
        LocalDateTime expiresAt
) {
}
