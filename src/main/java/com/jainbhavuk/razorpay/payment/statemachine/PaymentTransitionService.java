package com.jainbhavuk.razorpay.payment.statemachine;

import com.jainbhavuk.razorpay.common.enums.PaymentActor;
import com.jainbhavuk.razorpay.common.enums.PaymentEvent;
import com.jainbhavuk.razorpay.common.enums.PaymentStatus;
import com.jainbhavuk.razorpay.payment.entity.Payment;
import com.jainbhavuk.razorpay.payment.entity.PaymentTransitionLog;
import com.jainbhavuk.razorpay.payment.repository.PaymentTransitionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentTransitionService {
    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus apply(Payment payment, PaymentEvent paymentEvent) {
        PaymentStatus next = paymentStateMachine.transition(payment.getStatus(), paymentEvent);

        paymentTransitionLogRepository.save(PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(payment.getStatus())
                .toStatus(next)
                .occuredAt(java.time.LocalDateTime.now())
                .event(paymentEvent)
                .actor(PaymentActor.SYSTEM)
                .build());

        payment.setStatus(next);

        return next;
    }
}
