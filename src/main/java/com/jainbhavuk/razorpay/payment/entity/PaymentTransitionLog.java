package com.jainbhavuk.razorpay.payment.entity;

import com.jainbhavuk.razorpay.common.entity.BaseEntity;
import com.jainbhavuk.razorpay.common.enums.PaymentActor;
import com.jainbhavuk.razorpay.common.enums.PaymentEvent;
import com.jainbhavuk.razorpay.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment_transition_log", indexes = {
        @Index(name = "idx_payment_transition_log_payment_id", columnList = "payment_id")
})
public class PaymentTransitionLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(length = 255, name = "from_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus fromStatus;

    @Column(length = 255, name = "to_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus toStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "event", nullable = false, length = 100)
    private PaymentEvent event;

    @Column(length = 255, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentActor actor;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occuredAt;
}
