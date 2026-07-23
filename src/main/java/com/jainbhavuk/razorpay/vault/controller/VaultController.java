package com.jainbhavuk.razorpay.vault.controller;

import com.jainbhavuk.razorpay.merchant.security.MerchantContext;
import com.jainbhavuk.razorpay.vault.dto.request.TokenizeRequest;
import com.jainbhavuk.razorpay.vault.dto.response.TokenizeResponse;
import com.jainbhavuk.razorpay.vault.service.VaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/v1/vault")
@RequiredArgsConstructor
public class VaultController {

    private final VaultService vaultService;
    private final MerchantContext merchantContext;

    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@Valid TokenizeRequest request) {
        TokenizeResponse response = vaultService.tokenize(request,merchantContext.getMerchantId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
