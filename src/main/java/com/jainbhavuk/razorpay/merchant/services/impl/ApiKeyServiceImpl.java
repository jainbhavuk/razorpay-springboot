package com.jainbhavuk.razorpay.merchant.services.impl;

import com.jainbhavuk.razorpay.common.exception.ResourceNotFoundException;
import com.jainbhavuk.razorpay.common.util.RandomizerUtil;
import com.jainbhavuk.razorpay.merchant.dto.request.ApiKeyCreateRequest;
import com.jainbhavuk.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.jainbhavuk.razorpay.merchant.dto.response.ApiKeyResponse;
import com.jainbhavuk.razorpay.merchant.entity.ApiKey;
import com.jainbhavuk.razorpay.merchant.entity.Merchant;
import com.jainbhavuk.razorpay.merchant.mapper.ApiKeyMapper;
import com.jainbhavuk.razorpay.merchant.repository.ApiKeyRepository;
import com.jainbhavuk.razorpay.merchant.repository.MerchantRepository;
import com.jainbhavuk.razorpay.merchant.services.ApiKeyService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    private final MerchantRepository merchantRepository;
    private final ApiKeyMapper apiKeyMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public ApiKeyCreateResponse create(UUID merchantId, ApiKeyCreateRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(() -> new ResourceNotFoundException("Merchant", merchantId));

        String key = "rzp_" + request.environment().toString().toLowerCase() + "_" + RandomizerUtil.randomBase64(24);
        String rawSecret = RandomizerUtil.randomBase64(40);

        ApiKey apiKey = ApiKey.builder()
                .keyId(key)
                .keySecretHash(bCryptPasswordEncoder.encode(rawSecret))
                .environment(request.environment())
                .merchant(merchant)
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), apiKey.getKeyId(), rawSecret, apiKey.getEnvironment());
    }

    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {
        return apiKeyRepository.findByMerchant_Id(merchantId).stream().map(apiKey ->
                apiKeyMapper.toApiKeyResponseList(apiKey)).toList();
    }

    @Transactional
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId).orElseThrow(() -> new ResourceNotFoundException("API KEY", keyId));

        UUID merchantIdFromRepo = apiKey.getMerchant().getId();

        if(merchantIdFromRepo.equals(merchantId)) {
            apiKey.setEnabled(false);
        }
    }

    @Transactional
    public ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId).orElseThrow(() -> new ResourceNotFoundException("API KEY", keyId));

        if(!apiKey.isEnabled()) throw new RuntimeException("API Key is disabled, cannot rotate");

        if(apiKey.getMerchant().getId().equals(merchantId)) {
            String rawSecret = RandomizerUtil.randomBase64(40);

            apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());
            apiKey.setKeySecretHash(bCryptPasswordEncoder.encode(rawSecret));
            apiKey.setRotatedAt(LocalDateTime.now());
            apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));

            return new ApiKeyCreateResponse(apiKey.getId(), apiKey.getKeyId(), rawSecret, apiKey.getEnvironment());
        }
        else {
            throw new ResourceNotFoundException("MERCHANT_API_KEY_COMBO_NOT_VALID", keyId);
        }
    }
}
