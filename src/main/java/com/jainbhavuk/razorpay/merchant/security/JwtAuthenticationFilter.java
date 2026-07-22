package com.jainbhavuk.razorpay.merchant.security;

import com.jainbhavuk.razorpay.merchant.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final MerchantContext merchantContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String tokenFromRequest = request.getHeader("Authorization");

            if (tokenFromRequest == null || !tokenFromRequest.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String extractToken = tokenFromRequest.split("Bearer ")[1];

            Claims claims = jwtUtil.verifyAccessToken(extractToken);

            if (claims != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Authentication auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role").toString()))
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Before Setting Merchant Context");
                merchantContext.setMerchantId(UUID.fromString(jwtUtil.extractMerchantId(claims)));

                log.info("Merchant Context Set For: {}", merchantContext.getMerchantId());
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
