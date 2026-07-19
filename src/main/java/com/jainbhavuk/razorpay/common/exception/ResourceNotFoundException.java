package com.jainbhavuk.razorpay.common.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private UUID identifier;

    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(resourceName + " not found: " + identifier);
        this.resourceName = resourceName;
        this.identifier = (UUID) identifier;
    }
}
