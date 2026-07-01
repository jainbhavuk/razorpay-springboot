package com.jainbhavuk.razorpay.payment.service.impl;

import com.jainbhavuk.razorpay.common.enums.OrderStatus;
import com.jainbhavuk.razorpay.common.enums.PaymentStatus;
import com.jainbhavuk.razorpay.common.exception.BusinessRuleViolationException;
import com.jainbhavuk.razorpay.common.exception.ResourceNotFoundException;
import com.jainbhavuk.razorpay.payment.dto.request.PaymentInitRequest;
import com.jainbhavuk.razorpay.payment.dto.response.PaymentResponse;
import com.jainbhavuk.razorpay.payment.entity.OrderRecord;
import com.jainbhavuk.razorpay.payment.entity.Payment;
import com.jainbhavuk.razorpay.payment.gateway.PaymentGatewayRouter;
import com.jainbhavuk.razorpay.payment.gateway.dto.PaymentRequest;
import com.jainbhavuk.razorpay.payment.gateway.dto.PaymentResult;
import com.jainbhavuk.razorpay.payment.mapper.PaymentMapper;
import com.jainbhavuk.razorpay.payment.repository.OrderRepository;
import com.jainbhavuk.razorpay.payment.repository.PaymentRepository;
import com.jainbhavuk.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Primary
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest request) {

        OrderRecord order = orderRepository.findByIdAndMerchantId(merchantId, request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.orderId()));

        if(order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.ATTEMPTED) {
            throw new BusinessRuleViolationException("Cannot initiate payment for order status: " + order.getStatus(), "CANNOT_INITIATE_PAYMENT");
        }

        Payment newPayment = Payment.builder()
                .merchantId(merchantId)
                .order(order)
                .merchantId(merchantId)
                .status(PaymentStatus.CREATED)
                .amount(order.getAmount())
                .method(request.method())
                .methodDetails(request.methodDetails())
                .build();

        newPayment = paymentRepository.save(newPayment);

        PaymentRequest paymentRequest = new PaymentRequest(
                newPayment.getId(),
                order.getId(),
                merchantId,
                order.getAmount(),
                newPayment.getMethod(),
                newPayment.getMethodDetails()
        );

        PaymentResult paymentResult = paymentGatewayRouter.initiate(paymentRequest);

        if(paymentResult instanceof PaymentResult.Pending pending) {

            newPayment.setProcessorReference(pending.registrationReference());

        } else if(paymentResult instanceof PaymentResult.Failure failure) {
            newPayment.setStatus(PaymentStatus.FAILED);
            newPayment.setErrorCode(failure.errorCode());
            newPayment.setErrorDescription(failure.errorDescription());
        }

        newPayment = paymentRepository.save(newPayment);
        orderRepository.save(order);
 
        return paymentMapper.toResponse(newPayment);
    }
}
