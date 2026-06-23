package com.jainbhavuk.razorpay.merchant.services;

import com.jainbhavuk.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.jainbhavuk.razorpay.merchant.dto.response.MerchantSignupResponse;

public interface AuthService {
    MerchantSignupResponse signup(MerchantSignupRequest request);
}
