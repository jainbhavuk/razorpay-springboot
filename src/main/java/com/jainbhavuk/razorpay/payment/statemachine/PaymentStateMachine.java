package com.jainbhavuk.razorpay.payment.statemachine;

import com.jainbhavuk.razorpay.common.enums.PaymentEvent;
import com.jainbhavuk.razorpay.common.enums.PaymentStatus;
import com.jainbhavuk.razorpay.common.exception.InvalidStateTransitionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentStateMachine {
    private record Transition(PaymentStatus from, PaymentEvent event){}

    private static final Map<Transition, PaymentStatus> transitions = Map.ofEntries(
            Map.entry(new Transition(PaymentStatus.CREATED, PaymentEvent.AUTHORIZE_ATTEMPT), PaymentStatus.AUTHORIZING),
            Map.entry(new Transition(PaymentStatus.AUTHORIZING, PaymentEvent.AUTHORIZE_SUCCESS), PaymentStatus.AUTHORIZED),
            Map.entry(new Transition(PaymentStatus.AUTHORIZING, PaymentEvent.AUTHORIZE_FAIL), PaymentStatus.FAILED),
            Map.entry(new Transition(PaymentStatus.AUTHORIZED, PaymentEvent.CAPTURE_REQUEST), PaymentStatus.CAPTURING),
            Map.entry(new Transition(PaymentStatus.CAPTURING, PaymentEvent.CAPTURE_SUCCESS), PaymentStatus.CAPTURED),
            Map.entry(new Transition(PaymentStatus.CAPTURING, PaymentEvent.CAPTURE_FAIL), PaymentStatus.AUTHORIZED),
            Map.entry(new Transition(PaymentStatus.CAPTURED, PaymentEvent.REFUND_INIT), PaymentStatus.PARTIAL_REFUNDED),
            Map.entry(new Transition(PaymentStatus.CAPTURED, PaymentEvent.SETTLE), PaymentStatus.SETTLED),
            Map.entry(new Transition(PaymentStatus.SETTLED, PaymentEvent.REFUND_INIT), PaymentStatus.PARTIAL_REFUNDED),
            Map.entry(new Transition(PaymentStatus.PARTIAL_REFUNDED, PaymentEvent.REFUND_COMPLETE), PaymentStatus.REFUNDED),
            Map.entry(new Transition(PaymentStatus.CAPTURED, PaymentEvent.REFUND_COMPLETE), PaymentStatus.REFUNDED),
            Map.entry(new Transition(PaymentStatus.CREATED, PaymentEvent.CANCEL), PaymentStatus.CANCELLED),
            Map.entry(new Transition(PaymentStatus.AUTHORIZING, PaymentEvent.CANCEL), PaymentStatus.CANCELLED),
            Map.entry(new Transition(PaymentStatus.AUTHORIZED, PaymentEvent.CAPTURE_TIMEOUT), PaymentStatus.CANCELLED)
    );

    public PaymentStatus transition(PaymentStatus paymentStatus, PaymentEvent paymentEvent) {
        PaymentStatus next = transitions.get(new Transition(paymentStatus, paymentEvent));

        if(next == null) {
            throw new InvalidStateTransitionException("Invalid transition from " + paymentStatus + " on event " + paymentEvent);
        }

        return next;
    }
}
