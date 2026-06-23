package com.jainbhavuk.razorpay.vault.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vault_card")
public class VaultCard {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 4)
    private String lastFour;

    @Column(nullable = false, length = 6)
    private String bin;

    @Column(nullable = false)
    private byte[] encryptedPan;

    @Column(nullable = false)
    private byte[] encryptedDek;

    @Column(nullable = false)
    private String brand;

    @Column(length = 2, nullable = false)
    private String expiryMonth;

    @Column(length = 4, nullable = false)
    private String expiryYear;

    @Column(length = 100, nullable = false)
    private String cardHolderName;

    private LocalDateTime deletedAt;


}
