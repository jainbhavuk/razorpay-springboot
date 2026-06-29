package com.jainbhavuk.razorpay.payment.repository;

import com.jainbhavuk.razorpay.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrder_IdAndMerchantId(UUID orderId, UUID merchantId);
}
