package com.jainbhavuk.razorpay.merchant.dto.request;

import com.jainbhavuk.razorpay.common.enums.Environment;

public record ApiKeyCreateRequest(
        Environment environment
) {
}
