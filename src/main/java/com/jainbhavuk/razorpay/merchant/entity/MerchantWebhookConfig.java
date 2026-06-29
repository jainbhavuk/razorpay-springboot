package com.jainbhavuk.razorpay.merchant.entity;

import com.jainbhavuk.razorpay.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "merchant_webhook_config", indexes = {
        @Index(name = "idx_webhook_merchant_id", columnList = "merchant_id, enabled")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantWebhookConfig extends BaseEntity {

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

}
