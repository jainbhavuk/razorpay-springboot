package com.jainbhavuk.razorpay.payment.processor.strategy;

import com.jainbhavuk.razorpay.common.util.RandomizerUtil;
import com.jainbhavuk.razorpay.payment.processor.PaymentProcessor;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorResponse;
import org.springframework.stereotype.Component;

@Component
public class NetBankingPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        final String BANK_CODE_FAIL = "BANK_CODE_FAIL";

        String bankCode = request.methodDetails() != null ?
                request.methodDetails().get("bank").toString() : null;

        /**
         * Simulating a failure scenario for UPI payments when the bank code is "fail@okaxis".
         * In a real-world scenario, this would involve calling the UPI payment gateway and handling the response.
         */
        if(BANK_CODE_FAIL.equals(bankCode)) {
            return new PaymentProcessorResponse.Failure(
                    "Net Banking payment failed due to invalid bank code: " + bankCode,
                    "Bank Rejected The TXN Registration"
            );
        }

        String processorRef = "NBK_PROCESSOR" + RandomizerUtil.randomBase64(16);

//        String redirectRef = "http://www.redirectbank.com/" + processorRef;

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
