package com.jainbhavuk.razorpay.merchant.repository;

import com.jainbhavuk.razorpay.merchant.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByMerchant_Id(UUID id);

    Optional<AppUser> findByEmail(String email);
}
