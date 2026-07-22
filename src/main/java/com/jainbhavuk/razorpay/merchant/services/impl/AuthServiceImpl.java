package com.jainbhavuk.razorpay.merchant.services.impl;

import com.jainbhavuk.razorpay.common.enums.MerchantStatus;
import com.jainbhavuk.razorpay.common.enums.Role;
import com.jainbhavuk.razorpay.common.exception.DuplicateResourceException;
import com.jainbhavuk.razorpay.common.exception.ResourceNotFoundException;
import com.jainbhavuk.razorpay.merchant.dto.request.LoginRequest;
import com.jainbhavuk.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.jainbhavuk.razorpay.merchant.dto.response.LoginResponse;
import com.jainbhavuk.razorpay.merchant.dto.response.MerchantSignupResponse;
import com.jainbhavuk.razorpay.merchant.entity.AppUser;
import com.jainbhavuk.razorpay.merchant.entity.Merchant;
import com.jainbhavuk.razorpay.merchant.mapper.MerchantMapper;
import com.jainbhavuk.razorpay.merchant.repository.AppUserRepository;
import com.jainbhavuk.razorpay.merchant.repository.MerchantRepository;
import com.jainbhavuk.razorpay.merchant.security.util.JwtUtil;
import com.jainbhavuk.razorpay.merchant.services.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.ADMIN)
                .merchant(merchant)
                .build();

        appUserRepository.save(appUser);

        return merchantMapper.toMerchantSignupResponse(merchant);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        AppUser appUser = appUserRepository.findByEmail(request.email()).orElseThrow(
                () -> new ResourceNotFoundException("AppUser", request.email())
        );

        String jwtToken = jwtUtil.generateAccessToken(request.email(), appUser.getMerchant().getId(), appUser.getRole().toString());

        return new LoginResponse(jwtToken);
    }
}
