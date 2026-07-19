package com.jainbhavuk.razorpay.vault.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {ExpiryYearValidator.class})
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface ExpiryYear {
    String message() default "Expiry year must be greater than or equal to current year";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
