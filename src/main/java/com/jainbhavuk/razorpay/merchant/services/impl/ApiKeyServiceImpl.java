package com.jainbhavuk.razorpay.merchant.services.impl;

import com.jainbhavuk.razorpay.common.exception.ResourceNotFoundException;
import com.jainbhavuk.razorpay.merchant.dto.request.ApiKeyCreateRequest;
import com.jainbhavuk.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.jainbhavuk.razorpay.merchant.entity.ApiKey;
import com.jainbhavuk.razorpay.merchant.entity.Merchant;
import com.jainbhavuk.razorpay.merchant.repository.ApiKeyRepository;
import com.jainbhavuk.razorpay.merchant.repository.MerchantRepository;
import com.jainbhavuk.razorpay.merchant.services.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    private final MerchantRepository merchantRepository;

    public ApiKeyCreateResponse create(UUID merchantId, ApiKeyCreateRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(() -> new ResourceNotFoundException("Merchant", merchantId));

        String key = "rzp_" + request.environment().toString().toLowerCase()+"big_random_secret";
        String rawSecret = "big_random_secret";

        ApiKey apiKey = ApiKey.builder()
                .keyId(key)
                .keySecretHash(rawSecret)
                .environment(request.environment())
                .merchant(merchant)
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), apiKey.getKeyId(), rawSecret, apiKey.getEnvironment());
    }
}
