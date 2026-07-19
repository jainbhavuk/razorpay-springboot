package com.jainbhavuk.razorpay.vault.service.impl;

import com.jainbhavuk.razorpay.common.entity.Money;
import com.jainbhavuk.razorpay.common.enums.CardBrand;
import com.jainbhavuk.razorpay.common.exception.ResourceNotFoundException;
import com.jainbhavuk.razorpay.common.util.RandomizerUtil;
import com.jainbhavuk.razorpay.payment.processor.PaymentProcessorRouter;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.jainbhavuk.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.jainbhavuk.razorpay.vault.config.VaultEncryptionConfig;
import com.jainbhavuk.razorpay.vault.dto.request.TokenizeRequest;
import com.jainbhavuk.razorpay.vault.dto.response.TokenizeResponse;
import com.jainbhavuk.razorpay.vault.entity.CardToken;
import com.jainbhavuk.razorpay.vault.entity.VaultCard;
import com.jainbhavuk.razorpay.vault.repository.CardTokenRepository;
import com.jainbhavuk.razorpay.vault.repository.VaultRepository;
import com.jainbhavuk.razorpay.vault.service.VaultService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VaultServiceImpl implements VaultService {

    private final VaultRepository vaultRepository;
    private final CardTokenRepository cardTokenRepository;
    private final AesBytesEncryptor aesBytesEncryptor;
    private final PaymentProcessorRouter paymentProcessorRouter;

    @Override
    @Transactional
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {

        String lastFour = request.pan().substring(request.pan().length() - 4);
        String bin = request.pan().substring(0, 6);

        CardBrand brand = getCardBrand(bin);

        byte[] dek = KeyGenerators.secureRandom(32).generateKey();
        byte[] encryptedPan = VaultEncryptionConfig.panEncryptor(dek)
                .encrypt(request.pan().getBytes(StandardCharsets.UTF_8));
        byte[] encryptedDek = aesBytesEncryptor.encrypt(dek);

        VaultCard vaultCard = VaultCard.builder().
                cardHolderName(request.cardHolderName())
                .encryptedPan(encryptedPan)
                .encryptedDek(encryptedDek)
                .lastFour(lastFour)
                .bin(bin)
                .brand(brand)
                .expiryMonth(request.expiryMonth().toString())
                .expiryYear(request.expiryYear().toString())
                .build();

        vaultCard = vaultRepository.save(vaultCard);

        CardToken cardToken = CardToken.builder()
                .token("tkn" + RandomizerUtil.randomBase64(32))
                .vaultCard(vaultCard)
                .merchant(merchantId)
                .customer(request.customerId())
                .build();

        cardTokenRepository.save(cardToken);

        return new TokenizeResponse(
                cardToken.getToken(),
                vaultCard.getLastFour(),
                vaultCard.getBrand(),
                vaultCard.getExpiryMonth(),
                vaultCard.getExpiryYear()
        );
    }

    @Override
    public PaymentProcessorResponse charge(UUID paymentId, String cardToken, Money amount, Map<String, Object> methodDetails) {
       CardToken receivedCardToken = cardTokenRepository.findByTokenAndRevokedAtIsNull(cardToken).orElseThrow(() -> new ResourceNotFoundException("TOKEN", cardToken));
       VaultCard receivedVaultCard = receivedCardToken.getVaultCard();

       byte[] panBytes = null;

       try {
           byte[] dek = aesBytesEncryptor.decrypt(receivedVaultCard.getEncryptedDek());

           panBytes = VaultEncryptionConfig.panEncryptor(dek).decrypt(receivedVaultCard.getEncryptedPan());
           String pan = new String(panBytes, StandardCharsets.UTF_8);
           String expiry = receivedVaultCard.getExpiryMonth() + "/" + receivedVaultCard.getExpiryYear();

           PaymentProcessorRequest request = PaymentProcessorRequest.card(paymentId, pan, expiry, amount, methodDetails);

           PaymentProcessorResponse response = paymentProcessorRouter.charge(request);

           log.info("Payment processor response for paymentId {}: {}", paymentId, response);

           Arrays.fill(panBytes, (byte) 0);
           pan = null;

           return response;
       }catch (Exception e) {
           log.warn("Exception occurred while charging paymentId {}: {}", paymentId, e.getMessage());
           return new PaymentProcessorResponse.Failure("CHARGE_ERROR", "Failed to charge the card");
       }finally {
              if (panBytes != null) {
                Arrays.fill(panBytes, (byte) 0);
              }
       }
    }

    private CardBrand getCardBrand(String cardBin) {
        return switch (cardBin) {
            case "4" -> CardBrand.VISA;
            case "5", "2" -> CardBrand.MASTERCARD;
            case "3" -> CardBrand.AMEX;
            default -> CardBrand.RUPAY;
        };
    }
}
