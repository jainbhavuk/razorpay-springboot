package com.jainbhavuk.razorpay.merchant.dto.response;

import com.jainbhavuk.razorpay.common.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiKeyResponse(
        UUID id,
        String keyId,
        Environment environment,
        Boolean isEnabled,
        LocalDateTime createdAt,
        LocalDateTime lastUsedAt) {
}
