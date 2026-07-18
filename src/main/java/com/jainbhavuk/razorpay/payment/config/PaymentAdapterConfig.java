package com.jainbhavuk.razorpay.payment.config;

import com.jainbhavuk.razorpay.common.enums.PaymentMethod;
import com.jainbhavuk.razorpay.payment.gateway.adapter.PaymentAdapter;
import com.jainbhavuk.razorpay.payment.gateway.adapter.CardPaymentAdapter;
import com.jainbhavuk.razorpay.payment.gateway.adapter.UpiPaymentAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentAdapterConfig {

    private final CardPaymentAdapter cardPaymentAdapter;
    private final UpiPaymentAdapter upiPaymentAdapter;
    private final UpiPaymentAdapter netBankingPaymentAdapter;

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap() {
        return Map.of(
                PaymentMethod.CARD, cardPaymentAdapter,
                PaymentMethod.UPI, upiPaymentAdapter,
                PaymentMethod.NETBANKING, netBankingPaymentAdapter
        );
    }
}
