package com.jainbhavuk.razorpay.merchant.services;

import com.jainbhavuk.razorpay.merchant.dto.request.ApiKeyCreateRequest;
import com.jainbhavuk.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.jainbhavuk.razorpay.merchant.dto.response.ApiKeyResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse create(UUID merchantId, ApiKeyCreateRequest request);
    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}
