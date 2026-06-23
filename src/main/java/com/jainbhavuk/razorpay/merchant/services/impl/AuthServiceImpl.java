package com.jainbhavuk.razorpay.merchant.services.impl;

import com.jainbhavuk.razorpay.common.enums.MerchantStatus;
import com.jainbhavuk.razorpay.common.enums.Role;
import com.jainbhavuk.razorpay.common.exception.DuplicateResourceException;
import com.jainbhavuk.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.jainbhavuk.razorpay.merchant.dto.response.MerchantSignupResponse;
import com.jainbhavuk.razorpay.merchant.entity.AppUser;
import com.jainbhavuk.razorpay.merchant.entity.Merchant;
import com.jainbhavuk.razorpay.merchant.repository.AppUserRepository;
import com.jainbhavuk.razorpay.merchant.repository.MerchantRepository;
import com.jainbhavuk.razorpay.merchant.services.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.DuplicateFormatFlagsException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRepository appUserRepository;

    @Transactional
    public MerchantSignupResponse signup (MerchantSignupRequest request) {
        if(merchantRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Merchant with email " + request.email() + " already exists", "DUPLICATE_MERCHANT");
        }

        Merchant merchant = Merchant.builder()

                .name(request.name())
                .email(request.email())
                .businessName(request.businessName())
                .businessType(request.businessType())
                .status(MerchantStatus.PENDING_KYC)
                .build();

        merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .passwordHash(request.password())
                .role(Role.ADMIN)
                .merchant(merchant)
                .build();

        appUserRepository.save(appUser);

        return new MerchantSignupResponse(
                merchant.getId(),
                merchant.getName(),
                merchant.getEmail(),
                merchant.getBusinessName(),
                merchant.getBusinessType(),
                merchant.getStatus()
        );
    }
}
