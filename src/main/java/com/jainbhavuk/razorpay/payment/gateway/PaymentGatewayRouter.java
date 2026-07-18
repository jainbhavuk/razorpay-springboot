package com.jainbhavuk.razorpay.payment.gateway;

import com.jainbhavuk.razorpay.common.enums.PaymentMethod;
import com.jainbhavuk.razorpay.payment.entity.Payment;
import com.jainbhavuk.razorpay.payment.gateway.adapter.PaymentAdapter;
import com.jainbhavuk.razorpay.payment.gateway.dto.PaymentRequest;
import com.jainbhavuk.razorpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

    private final Map<PaymentMethod, PaymentAdapter> paymentAdapters;

    public PaymentResult initiate(PaymentRequest paymentRequest) {
        PaymentAdapter paymentAdapter = paymentAdapters.get(paymentRequest.method());

        if(paymentAdapter == null) {
            throw new IllegalArgumentException("No Adapter Found For This Payment Method");
        }

        return paymentAdapter.initiate(paymentRequest);
    }

    public PaymentResult capture(PaymentMethod method, UUID paymentId) {
        PaymentAdapter paymentAdapter = paymentAdapters.get(method);

        if(paymentAdapter == null) {
            throw new IllegalArgumentException("No Adapter Found For This Payment Method");
        }

        return paymentAdapter.capture(paymentId);
    }
}
