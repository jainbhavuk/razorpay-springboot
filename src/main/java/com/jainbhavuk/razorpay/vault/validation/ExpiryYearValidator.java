package com.jainbhavuk.razorpay.vault.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class ExpiryYearValidator implements ConstraintValidator<ExpiryYear, Integer> {
    @Override
    public boolean isValid(Integer yearToCheck, ConstraintValidatorContext context) {
        if(yearToCheck == null) {
            return true;
        }

        int currentYear = Year.now().getValue();

        return currentYear <= yearToCheck;
    }
}
