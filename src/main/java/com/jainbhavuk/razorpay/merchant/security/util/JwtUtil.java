package com.jainbhavuk.razorpay.merchant.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String email, UUID merchantId, String role) {

        Instant now = Instant.now();

        String jwtToken = Jwts.builder()
                .signWith(getSecretKey())
                .expiration(Date.from(now.plusSeconds(3600)))
                .issuedAt(Date.from(now))
                .subject(email)
                .claim("role", role)
                .claim("merchantId", merchantId.toString())
                .compact();

        log.info("Generated JWT Token: {}", jwtToken);

        return jwtToken;
    }

    public Claims verifyAccessToken(String jwtToken) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public String extractMerchantId(Claims claims) {
        return claims.get("merchantId", String.class);
    }
}
