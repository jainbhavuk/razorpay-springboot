package com.jainbhavuk.razorpay.payment.processor.strategy;

import com.jainbhavuk.razorpay.payment.processor.PaymentProcessor;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }
}
