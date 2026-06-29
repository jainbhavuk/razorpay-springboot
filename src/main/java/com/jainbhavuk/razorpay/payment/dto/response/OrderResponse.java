package com.jainbhavuk.razorpay.payment.dto.response;

import com.jainbhavuk.razorpay.common.entity.Money;
import com.jainbhavuk.razorpay.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    UUID merchantId,
    String receipt,
    Money amount,
    Integer attempts,
    OrderStatus status,
    Map<String, Object> notes,
    LocalDateTime expiresAt,
    LocalDateTime createdAt
) {
}
