package com.jainbhavuk.razorpay.payment.entity;

import com.jainbhavuk.razorpay.common.entity.BaseEntity;
import com.jainbhavuk.razorpay.common.entity.Money;
import com.jainbhavuk.razorpay.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.SQLType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_record", indexes = {
        @Index(name = "idx_order_record_merchant_id", columnList = "id, merchant_id"),
        @Index(name = "idx_order_record_merchant_id", columnList = "merchant_id")
})
public class OrderRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // No Foreign Key Due To Cross Service Boundary.
    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.CREATED;

    @Column(name = "receipt")
    private String receipt;

    @Column(nullable = false)
    @Builder.Default
    private Integer attempts = 0;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> notes;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

}
