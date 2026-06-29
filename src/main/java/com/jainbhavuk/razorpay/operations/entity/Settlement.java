package com.jainbhavuk.razorpay.operations.entity;

import com.jainbhavuk.razorpay.common.entity.BaseEntity;
import com.jainbhavuk.razorpay.common.entity.Money;
import com.jainbhavuk.razorpay.common.enums.SettlementStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "settlement")
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "gross_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "gross_currency", nullable = false))
    })
    private Money grossAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "refund_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "refund_currency", nullable = false))
    })
    private Money refundAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "gst_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "gst_currency", nullable = false))
    })
    private Money gstAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "fee_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "fee_currency", nullable = false))
    })
    private Money feeAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "net_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "net_currency", nullable = false))
    })
    private Money netAmount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SettlementStatus status;

    private String bankReference;

    private LocalDateTime processedAt;
}
