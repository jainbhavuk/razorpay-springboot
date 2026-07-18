package com.jainbhavuk.razorpay.payment.config;

import com.jainbhavuk.razorpay.common.enums.PaymentMethod;
import com.jainbhavuk.razorpay.payment.processor.PaymentProcessor;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.jainbhavuk.razorpay.payment.processor.strategy.CardPaymentProcessor;
import com.jainbhavuk.razorpay.payment.processor.strategy.NetBankingPaymentProcessor;
import com.jainbhavuk.razorpay.payment.processor.strategy.UpiPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.smartcardio.Card;
import java.util.*;

@RequiredArgsConstructor
@Configuration
public class PaymentProcessorConfig {

    private final CardPaymentProcessor cardPaymentProcessor;
    private final UpiPaymentProcessor upiPaymentProcessor;
    private final NetBankingPaymentProcessor netBankingPaymentProcessor;

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap() {
        return Map.of(
                PaymentMethod.CARD, cardPaymentProcessor,
                PaymentMethod.UPI, upiPaymentProcessor,
                PaymentMethod.NETBANKING, netBankingPaymentProcessor
        );
    }
}
