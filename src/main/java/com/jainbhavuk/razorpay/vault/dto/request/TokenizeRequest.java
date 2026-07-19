package com.jainbhavuk.razorpay.vault.dto.request;

import com.jainbhavuk.razorpay.vault.validation.ExpiryYear;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.LuhnCheck;

import java.util.UUID;

public record TokenizeRequest(
        @NotBlank(message = "PAN cannot be blank")
        @LuhnCheck(message = "Invalid PAN number")
        @Pattern(regexp = "^[0-9]{13,19}$", message = "PAN must be between 13 and 19 digits")
        String pan,

        @NotBlank(message = "CVV cannot be blank")
        @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV can only have 3 or 4 digits")
        String cvv,

        @NotNull(message = "Expiry month cannot be null")
        @Min(value = 1, message = "Expiry month must be between 1 and 12")
        @Max(value = 12, message = "Expiry month must be between 1 and 12")
        Integer expiryMonth,

        @NotNull(message = "Expiry year cannot be null")
        @ExpiryYear(message = "Expiry year must be a valid current or future year")
        Integer expiryYear,

        UUID customerId,

        @Size(min = 3, message = "Card holder name must be at least 3 characters long")
        String cardHolderName
) {
}
