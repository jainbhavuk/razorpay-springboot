package com.jainbhavuk.razorpay.vault.service;

import com.jainbhavuk.razorpay.vault.dto.request.TokenizeRequest;
import com.jainbhavuk.razorpay.vault.dto.response.TokenizeResponse;

import java.util.UUID;

public interface VaultService {
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);
}
