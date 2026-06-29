package com.hcl.routersimulator.config;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "router.simulator")
public record RouterSimulatorProperties(
        @NotBlank String topic,
        @Min(1) int deviceCount,
        @Min(1000) long publishIntervalMs
) {
}