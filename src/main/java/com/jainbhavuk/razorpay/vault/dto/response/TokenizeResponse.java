package com.jainbhavuk.razorpay.vault.dto.response;

import com.jainbhavuk.razorpay.common.enums.CardBrand;

public record TokenizeResponse(
        String token,
        String lastFour,
        CardBrand brand,
        String  expiryMonth,
        String expiryYear
) {
}
