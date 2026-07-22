package com.jainbhavuk.razorpay.payment.repository;

import com.jainbhavuk.razorpay.common.enums.PaymentStatus;
import com.jainbhavuk.razorpay.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByOrder_IdAndMerchantId(UUID orderId, UUID merchantId);

    Optional<Payment> findByIdAndMerchantId(UUID id, UUID merchantId);

    List<Payment> findByStatusAndCreatedAtBefore(PaymentStatus paymentStatus, LocalDateTime globalWindow);
}
