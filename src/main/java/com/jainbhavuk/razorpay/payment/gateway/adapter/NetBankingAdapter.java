package com.jainbhavuk.razorpay.payment.gateway.adapter;

import com.jainbhavuk.razorpay.common.enums.PaymentMethod;
import com.jainbhavuk.razorpay.payment.gateway.dto.PaymentRequest;
import com.jainbhavuk.razorpay.payment.gateway.dto.PaymentResult;
import com.jainbhavuk.razorpay.payment.processor.PaymentProcessor;
import com.jainbhavuk.razorpay.payment.processor.PaymentProcessorRouter;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NetBankingAdapter implements PaymentAdapter {

    private final PaymentProcessorRouter paymentProcessorRouter;

    public PaymentResult initiate(PaymentRequest paymentRequest) {
        try {
            log.info("Initiating NetBanking payment for paymentId: {}",
                    paymentRequest.paymentId()
            );

            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.nonCard(
                    paymentRequest.paymentId(),
                    paymentRequest.amount(),
                    PaymentMethod.NETBANKING,
                    paymentRequest.methodDetails()
            );

            PaymentProcessorResponse paymentProcessorResponse = paymentProcessorRouter.charge(paymentProcessorRequest);

            return switch (paymentProcessorResponse) {
                case PaymentProcessorResponse.Failure failure ->
                        new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
                case PaymentProcessorResponse.Success success -> new PaymentResult.Success(success.bankReference());
                case PaymentProcessorResponse.Pending pending ->
                        new PaymentResult.Pending(pending.processorReference());
            };
        } catch (Exception e) {
            log.warn("Exception occurred while initiating NetBanking payment for paymentId: {}, error: {}",
                    paymentRequest.paymentId(),
                    e.getMessage()
            );

            return new PaymentResult.Failure("NETBANKING_INITIATION_ERROR", "Failed to initiate NetBanking payment");
        }
    }
}
