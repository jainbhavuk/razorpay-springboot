package com.jainbhavuk.razorpay.common.audit;

import com.jainbhavuk.razorpay.merchant.security.MerchantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    private final MerchantContext merchantContext;

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            String keyId = merchantContext.getKeyId();
            String merchantId = merchantContext.getMerchantId().toString();

            if (keyId != null && !keyId.isBlank()) return Optional.of(keyId);

            if (merchantId != null) return Optional.of(merchantId);
        } catch (Exception ignored) {}

        return Optional.of("SYSTEM");
    }
}
