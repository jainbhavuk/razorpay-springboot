package com.jainbhavuk.razorpay.merchant.security;

import com.jainbhavuk.razorpay.merchant.entity.ApiKey;
import com.jainbhavuk.razorpay.merchant.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final String BASIC_PREFIX = "Basic ";
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final ApiKeyRepository apiKeyRepository;
    private final MerchantContext merchantContext;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith(BASIC_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String[] credentials = decodeHeader(authHeader);
            if (credentials == null) {
                throw new BadRequestException("Malformed API Key Header");
            }

            String keyId = credentials[0];
            String rawSecret = credentials[1];

            ApiKey apiKey = apiKeyRepository.findByKeyId(keyId).orElseThrow(() ->
                    new BadRequestException("API Key Invalid")
            );

            if (!apiKey.isEnabled() || !secretMatches(rawSecret, apiKey)) {
                throw new BadRequestException("API Key not valid");
            }

            if (bCryptPasswordEncoder.matches(rawSecret, apiKey.getKeySecretHash())) {
                Authentication auth = new UsernamePasswordAuthenticationToken(keyId, null,
                        List.of(new SimpleGrantedAuthority("ROLE_API_KEY"))
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Before Setting Merchant Context via API Key");

                merchantContext.setMerchantId(apiKey.getMerchant().getId());
                merchantContext.setKeyId(keyId);

                log.info("Merchant Context (API KEY) Set For Merchant ID: {}", merchantContext.getMerchantId());

                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request,response,null,e);
        }
    }

    private boolean secretMatches(String rawSecret, ApiKey apiKey) {
        if(bCryptPasswordEncoder.matches(rawSecret, apiKey.getKeySecretHash())) {
            return true;
        }

        boolean isApiKeyInGracePeriod = apiKey.getGracePeriodExpiresAt() != null
            && apiKey.getGracePeriodExpiresAt().isAfter(LocalDateTime.now());

        return isApiKeyInGracePeriod
                && apiKey.getPreviousKeySecretHash()!= null
                && bCryptPasswordEncoder.matches(rawSecret, apiKey.getPreviousKeySecretHash());
    }

    private String[] decodeHeader(String authHeader) {
        String encodedBase64Header = authHeader.substring(BASIC_PREFIX.length());
        String decodedHeader = new String(Base64.getDecoder().decode(encodedBase64Header));

        int colon = decodedHeader.indexOf(":");
        if (colon < 1) return null;

        return new String[] {decodedHeader.substring(0, colon), decodedHeader.substring(colon+1)};
    }

}
