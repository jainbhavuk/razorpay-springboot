package com.jainbhavuk.razorpay.payment.processor.strategy;

import com.jainbhavuk.razorpay.common.util.RandomizerUtil;
import com.jainbhavuk.razorpay.payment.processor.PaymentProcessor;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CardPaymentProcessor implements PaymentProcessor {

    private static final String PAN_CARD_DECLINED = "4000000000002";
    private static final String PAN_CARD_EXPIRED = "4000000000009";

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        String pan = request.pan();

        if(PAN_CARD_DECLINED.equals(pan)) {
            log.warn("Card Declined");

            return new PaymentProcessorResponse.Failure("CARD_DECLINED", "The card was declined by the bank");
        }

        if(PAN_CARD_EXPIRED.equals(pan)) {
            return new PaymentProcessorResponse.Failure("CARD_EXPIRED", "The card has expired");
        }

        String paymentProcessorRef = "CARD_PROCESSOR" + RandomizerUtil.randomBase64(16);

        return new PaymentProcessorResponse.Pending(paymentProcessorRef);
    }
}
