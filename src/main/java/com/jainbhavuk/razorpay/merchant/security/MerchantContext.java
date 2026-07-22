package com.jainbhavuk.razorpay.merchant.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@Getter
@Setter
@Component
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MerchantContext {
    private String email;
    private UUID merchantId;
}
