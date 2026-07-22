package com.jainbhavuk.razorpay.payment.simulator;

import com.jainbhavuk.razorpay.common.enums.ChaosMode;
import com.jainbhavuk.razorpay.common.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "payment.simulator")
@Getter
@Setter
public class SimulatorConfig {

    private Integer pollIntervalMs = 2000;
    private ChaosMode chaosMode = ChaosMode.NORMAL;

    private Map<String, MethodSimulatorConfig> methods = new HashMap<>();

    public SimulatorConfig.MethodSimulatorConfig configFor(PaymentMethod method) {
        return methods.getOrDefault(method.name(), new MethodSimulatorConfig(1, 5, 80));
    }

    public static record MethodSimulatorConfig(
            Integer minDelaySeconds,
            Integer maxDelaySeconds,
            Integer successRate
    ) {}

}
