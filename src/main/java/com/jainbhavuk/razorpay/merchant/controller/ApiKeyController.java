package com.jainbhavuk.razorpay.merchant.controller;

import com.jainbhavuk.razorpay.merchant.dto.request.ApiKeyCreateRequest;
import com.jainbhavuk.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.jainbhavuk.razorpay.merchant.dto.response.ApiKeyResponse;
import com.jainbhavuk.razorpay.merchant.services.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
public class ApiKeyController {
    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> create(@PathVariable(name = "merchantId") UUID merchantId,
                                                       @Valid @RequestBody ApiKeyCreateRequest request
                                         ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(apiKeyService.create(merchantId, request));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> getAllApiKeys(@PathVariable(name = "merchantId") UUID merchantId) {
        return ResponseEntity.status(HttpStatus.OK).body(apiKeyService.listByMerchant(merchantId));
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<Void> revoke (@PathVariable(name = "merchantId") UUID merchantId, @PathVariable(name = "keyId") UUID keyId){
       apiKeyService.revoke(merchantId, keyId);
       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("{keyId}/rotate")
    public ResponseEntity<ApiKeyCreateResponse> rotate (@PathVariable(name = "merchantId") UUID merchantId, @PathVariable(name = "keyId") UUID keyId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(apiKeyService.rotate(merchantId, keyId));
    }

}
