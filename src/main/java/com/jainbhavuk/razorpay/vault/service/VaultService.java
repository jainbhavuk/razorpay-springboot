package com.jainbhavuk.razorpay.vault.service;

import com.jainbhavuk.razorpay.common.entity.Money;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.jainbhavuk.razorpay.vault.dto.request.TokenizeRequest;
import com.jainbhavuk.razorpay.vault.dto.response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String cardToken, Money amount, Map<String, Object> methodDetails);
}
