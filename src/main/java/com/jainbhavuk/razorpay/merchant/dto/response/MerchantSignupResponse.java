package com.jainbhavuk.razorpay.merchant.dto.response;

import com.jainbhavuk.razorpay.common.enums.BusinessType;
import com.jainbhavuk.razorpay.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantSignupResponse(
    UUID id,
    String name,
    String email,
    String businessName,
    BusinessType businessType,
    MerchantStatus status
) {
}
