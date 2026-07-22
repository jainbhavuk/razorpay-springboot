package com.jainbhavuk.razorpay.merchant.services;

import com.jainbhavuk.razorpay.merchant.dto.request.LoginRequest;
import com.jainbhavuk.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.jainbhavuk.razorpay.merchant.dto.response.LoginResponse;
import com.jainbhavuk.razorpay.merchant.dto.response.MerchantSignupResponse;
import jakarta.validation.Valid;

public interface AuthService {
    MerchantSignupResponse signup(MerchantSignupRequest request);

    LoginResponse login(LoginRequest request);
}
