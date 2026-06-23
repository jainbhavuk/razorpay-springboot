package com.jainbhavuk.razorpay.operations.entity;

import com.jainbhavuk.razorpay.common.enums.WebhookEventStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
public class WebhookEvent {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private WebhookEventStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "jsonb")
    private Map<String, Object> payload;

    @Column(length = 255, nullable = false)
    private String targetUrl;

    @Column(nullable = false)
    private String signature;

    @Column(nullable = false)
    private Integer attempts = 0;

    private Integer lastResponseCode;

    @Column(length = 1000)
    private String lastResponseBody;

    private LocalDateTime nextRetryAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime deliveredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
