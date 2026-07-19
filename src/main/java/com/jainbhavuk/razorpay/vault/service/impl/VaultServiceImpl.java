package com.jainbhavuk.razorpay.vault.service.impl;

import com.jainbhavuk.razorpay.vault.dto.request.TokenizeRequest;
import com.jainbhavuk.razorpay.vault.dto.response.TokenizeResponse;
import com.jainbhavuk.razorpay.vault.repository.CardTokenRepository;
import com.jainbhavuk.razorpay.vault.repository.VaultRepository;
import com.jainbhavuk.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VaultServiceImpl implements VaultService {

    private final VaultRepository vaultRepository;
    private final CardTokenRepository cardTokenRepository;

    @Override
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {
        return null;
    }
}
