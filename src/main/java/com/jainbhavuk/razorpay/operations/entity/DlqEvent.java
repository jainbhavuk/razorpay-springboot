package com.jainbhavuk.razorpay.operations.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "dlq_event")
public class DlqEvent {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "webhook_event_id")
    private WebhookEvent webhookEvent;

    @Column(nullable = false)
    private UUID merchantId;

    private String finalError;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> payload;

    private LocalDateTime movedAt;

    private LocalDateTime replayedAt;
}
