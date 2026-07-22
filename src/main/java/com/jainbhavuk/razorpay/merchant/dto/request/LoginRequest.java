package com.jainbhavuk.razorpay.merchant.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        @NotNull
        String email,

        @NotBlank(message = "Password is required")
        @NotNull
        String password
) {
}
