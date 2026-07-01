package com.jainbhavuk.razorpay.payment.config;

import com.jainbhavuk.razorpay.common.enums.PaymentMethod;
import com.jainbhavuk.razorpay.payment.gateway.adapter.PaymentAdapter;
import com.jainbhavuk.razorpay.payment.gateway.adapter.CardPaymentAdapter;
import com.jainbhavuk.razorpay.payment.gateway.adapter.UpiPaymentAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentAdapterConfig {

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap() {
        return Map.of(
                PaymentMethod.CARD, new CardPaymentAdapter(),
                PaymentMethod.UPI, new UpiPaymentAdapter(),
                PaymentMethod.NETBANKING, new UpiPaymentAdapter()
        );
    }
}
