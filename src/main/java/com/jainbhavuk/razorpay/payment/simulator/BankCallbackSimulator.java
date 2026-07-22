package com.jainbhavuk.razorpay.payment.simulator;

import com.jainbhavuk.razorpay.common.enums.ChaosMode;
import com.jainbhavuk.razorpay.common.enums.PaymentStatus;
import com.jainbhavuk.razorpay.common.util.RandomizerUtil;
import com.jainbhavuk.razorpay.payment.entity.Payment;
import com.jainbhavuk.razorpay.payment.repository.PaymentRepository;
import com.jainbhavuk.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class BankCallbackSimulator {
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final SimulatorConfig simulatorConfig;

//    @Scheduled(fixedDelay = 5000)
    public void processCallback() {
        log.info("Running Bank Callback Simulator");

        LocalDateTime globalWindow = LocalDateTime.now().minusSeconds(1);

        List<Payment> candidates = paymentRepository.findByStatusAndCreatedAtBefore(PaymentStatus.AUTHORIZING, globalWindow);

        if(candidates.isEmpty()) return;

        for(Payment payment : candidates) {
            simulateCallback(payment);
        }

    }

    private void simulateCallback(Payment payment) {
        SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig = simulatorConfig.configFor(payment.getMethod());

        LocalDateTime dueAt = dueAt(payment, methodSimulatorConfig);

        if(dueAt.isAfter(LocalDateTime.now())) {
            log.debug("Payment {} is not due for callback yet. Due at: {}", payment.getId(), dueAt);
            return;
        }

        ChaosMode chaosMode = simulatorConfig.getChaosMode();

        switch (chaosMode) {
            case SUCCESS -> resolve(payment, true);
            case FAILURE -> resolve(payment, false);
            case TIMEOUT -> log.debug("Payment Timeout");
            case SLOW, NORMAL -> resolve(payment, shouldApprove(methodSimulatorConfig));
        }
    }

    private void resolve(Payment payment, boolean approved) {
        if(approved) {
          String bankRef = "SIM_BANK_REF" + RandomizerUtil.randomBase64(16);
          paymentService.resolveAuthorization(payment.getId(), true, bankRef, null, null);
        }
        else {
            paymentService.resolveAuthorization(payment.getId(), false, null, "SIM_BANK_ERROR_CODE" , "Simulated Bank Declined");
        }
    }

    private boolean shouldApprove(SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig) {
        int bucket = (int) (Math.random()*100);
        return bucket < methodSimulatorConfig.successRate();
    }

    private LocalDateTime dueAt(Payment payment, SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig) {
        int range = methodSimulatorConfig.maxDelaySeconds() - methodSimulatorConfig.minDelaySeconds();
        int delaySeconds = methodSimulatorConfig.minDelaySeconds() + (int)(Math.random() * range);

        if(simulatorConfig.getChaosMode() == ChaosMode.SLOW) {
            delaySeconds*=2;
        }

        return payment.getCreatedAt().plusSeconds(delaySeconds);
    }
}
