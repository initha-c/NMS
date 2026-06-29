package com.hcl.routersimulator.web;

import com.hcl.routersimulator.config.RouterSimulatorProperties;
import com.hcl.routersimulator.service.RouterMetricProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RouterSimulatorController {

    private final RouterMetricProducer producer;
    private final RouterSimulatorProperties properties;

    public RouterSimulatorController(
            RouterMetricProducer producer,
            RouterSimulatorProperties properties
    ) {
        this.producer = producer;
        this.properties = properties;
    }

    @GetMapping("/simulate-now")
    public Map<String, Object> simulateNow() {
        producer.publishMetricsForAllRouters();

        return Map.of(
                "message", "Router metrics published to Kafka",
                "topic", properties.topic(),
                "deviceCount", properties.deviceCount()
        );
    }

    @GetMapping("/config")
    public RouterSimulatorProperties config() {
        return properties;
    }
}