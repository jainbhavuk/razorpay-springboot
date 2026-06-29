package com.jainbhavuk.razorpay.merchant.services.impl;

import com.jainbhavuk.razorpay.common.enums.MerchantStatus;
import com.jainbhavuk.razorpay.common.enums.Role;
import com.jainbhavuk.razorpay.common.exception.DuplicateResourceException;
import com.jainbhavuk.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.jainbhavuk.razorpay.merchant.dto.response.MerchantSignupResponse;
import com.jainbhavuk.razorpay.merchant.entity.AppUser;
import com.jainbhavuk.razorpay.merchant.entity.Merchant;
import com.jainbhavuk.razorpay.merchant.mapper.MerchantMapper;
import com.jainbhavuk.razorpay.merchant.repository.AppUserRepository;
import com.jainbhavuk.razorpay.merchant.repository.MerchantRepository;
import com.jainbhavuk.razorpay.merchant.services.AuthService;
import org.springframework.transaction.annotation.Transactional;
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
    private final MerchantMapper merchantMapper;

    @Transactional
    public MerchantSignupResponse signup (MerchantSignupRequest request) {
        if(merchantRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Merchant with email " + request.email() + " already exists", "DUPLICATE_MERCHANT");
        }

        Merchant merchant = merchantMapper.toEntityFromMerchantSignupRequest(request);
        merchant.setStatus(MerchantStatus.PENDING_KYC);

        merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .passwordHash(request.password())
                .role(Role.ADMIN)
                .merchant(merchant)
                .build();

        appUserRepository.save(appUser);

        return merchantMapper.toMerchantSignupResponse(merchant);
    }
}
