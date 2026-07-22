package com.jainbhavuk.razorpay.payment.service.impl;

import com.jainbhavuk.razorpay.common.enums.OrderStatus;
import com.jainbhavuk.razorpay.common.enums.PaymentEvent;
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
import com.jainbhavuk.razorpay.payment.statemachine.PaymentTransitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final PaymentTransitionService paymentTransitionService;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest request) {

        OrderRecord order = orderRepository.findByIdAndMerchantId(request.orderId(), merchantId)
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

        paymentTransitionService.apply(newPayment, PaymentEvent.AUTHORIZE_ATTEMPT);

        PaymentResult paymentResult = paymentGatewayRouter.initiate(paymentRequest);

        if(paymentResult instanceof PaymentResult.Pending pending) {

            newPayment.setProcessorReference(pending.registrationReference());

        } else if(paymentResult instanceof PaymentResult.Failure failure) {
            paymentTransitionService.apply(newPayment, PaymentEvent.AUTHORIZE_FAIL);
            newPayment.setErrorCode(failure.errorCode());
            newPayment.setErrorDescription(failure.errorDescription());
        }

        newPayment = paymentRepository.save(newPayment);
        orderRepository.save(order);
 
        return paymentMapper.toResponse(newPayment);
    }

    @Override
    public PaymentResponse capture(UUID merchantId, UUID paymentId) {
        Payment payment = paymentRepository.findByIdAndMerchantId(paymentId, merchantId).orElseThrow(
                () -> new ResourceNotFoundException("PAYMENT", paymentId)
        );

        paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_REQUEST);

        PaymentResult paymentResult = paymentGatewayRouter.capture(payment.getMethod(), paymentId);

        if(paymentResult instanceof PaymentResult.Success){

            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
            payment.setCapturedAt(LocalDateTime.now());

            log.info("Payment captured successfully for paymentId: {}", paymentId);

        } else if(paymentResult instanceof PaymentResult.Failure)
        {
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_FAIL);
            payment.setErrorCode(((PaymentResult.Failure) paymentResult).errorCode());
            payment.setErrorDescription(((PaymentResult.Failure) paymentResult).errorDescription());

            log.warn("Payment capture failed for paymentId: {}", paymentId);
        }

         payment = paymentRepository.save(payment);

         return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    public void resolveAuthorization(UUID paymentId, boolean approved, String bankRef, String errorCode, String errorDescription) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new ResourceNotFoundException("PAYMENT", paymentId)
        );

        if(payment.getStatus() != PaymentStatus.AUTHORIZING) {
            log.warn("Payment authorization resolution received for paymentId: {} but payment is not in AUTHORIZING state. Current state: {}", paymentId, payment.getStatus());
            return;
        }

        OrderRecord orderRecord = payment.getOrder();

        if(approved) {
            paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_SUCCESS);
            payment.setProcessorReference(bankRef);
            payment.setAuthorizedAt(LocalDateTime.now());

            /** Auto Capture */

            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_REQUEST);
            PaymentResult capturedResult = paymentGatewayRouter.capture(payment.getMethod(), paymentId);

            if(capturedResult instanceof PaymentResult.Success) {
                paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
                payment.setCapturedAt(LocalDateTime.now());
                orderRecord.setStatus(OrderStatus.PAID);
            }
            else if(capturedResult instanceof PaymentResult.Failure) {
                paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_FAIL);
                payment.setErrorCode(((PaymentResult.Failure) capturedResult).errorCode());
                payment.setErrorDescription(((PaymentResult.Failure) capturedResult).errorDescription());
            }

        } else {
            paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
            payment.setErrorCode(errorCode);
            payment.setErrorDescription(errorDescription);
        }

        paymentRepository.save(payment);
        orderRepository.save(orderRecord);

    }
}
