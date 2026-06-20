package com.jainbhavuk.razorpay.merchant.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "merchant_webhook_config")
public class MerchantWebhookConfig {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(name = "target_url", nullable = false, length = 500)
    private String targetUrl;

    @Column(name = "event_type_filter")
    private String eventTypeFilter;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(length = 255)
    private String webhookSecretHash;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
