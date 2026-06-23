package com.jainbhavuk.razorpay.merchant.dto.request;

import com.jainbhavuk.razorpay.common.enums.BusinessType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MerchantSignupRequest(

        @NotNull(message = "Name cannot be null")
        @Size(max = 50, message = "Name cannot exceed 50 characters")
        String name,

        @NotNull(message = "Email cannot be null")
        @Email(message = "Email should be valid")
        String email,

        @NotNull(message = "Password cannot be null")
        @Size(min = 8, message = "Password should be at least 8 characters long")
        String password,

        @Size(max = 50, message = "Business name cannot exceed 50 characters")
        String businessName,

        BusinessType businessType
) {
}
