package com.jainbhavuk.razorpay.payment.gateway.adapter;

import com.jainbhavuk.razorpay.payment.gateway.dto.PaymentRequest;
import com.jainbhavuk.razorpay.payment.gateway.dto.PaymentResult;
import com.jainbhavuk.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardPaymentAdapter implements PaymentAdapter {

    private final VaultService vaultService;

    public PaymentResult initiate(PaymentRequest paymentRequest) {
        String cardToken = paymentRequest.methodDetails().get("token").toString();

        vaultService.charge(paymentRequest.paymentId(), cardToken, paymentRequest.amount(), paymentRequest.methodDetails());

        return null;
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("CARD_REF");
    }
}
