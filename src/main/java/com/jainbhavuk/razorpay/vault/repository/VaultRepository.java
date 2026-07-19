package com.jainbhavuk.razorpay.vault.repository;

import com.jainbhavuk.razorpay.vault.entity.VaultCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VaultRepository extends JpaRepository<VaultCard, UUID> {
}
