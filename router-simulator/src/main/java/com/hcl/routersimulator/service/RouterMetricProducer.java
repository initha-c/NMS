package com.hcl.routersimulator.service;

import com.hcl.routersimulator.config.RouterSimulatorProperties;
import com.hcl.routersimulator.model.RouterMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
public class RouterMetricProducer {

    private static final Logger log = LoggerFactory.getLogger(RouterMetricProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RouterSimulatorProperties properties;
    private final Random random = new Random();

    public RouterMetricProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            RouterSimulatorProperties properties
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.properties = properties;
    }

    public void publishMetricsForAllRouters() {
        for (int i = 1; i <= properties.deviceCount(); i++) {
            String deviceId = "router-" + i;

            RouterMetric metric = generateMetric(deviceId);
            String jsonMessage = toJson(metric);

            kafkaTemplate.send(properties.topic(), deviceId, jsonMessage)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            log.error("Failed to publish metric for deviceId={}", deviceId, exception);
                        } else {
                            log.info("Published metric to Kafka: {}", jsonMessage);
                        }
                    });
        }
    }

    private RouterMetric generateMetric(String deviceId) {
        double temperature = 30 + random.nextDouble(55);
        long numPackets = random.nextLong(20_000);
        boolean alive = random.nextDouble() > 0.05;

        return new RouterMetric(
                deviceId,
                Math.round(temperature * 100.0) / 100.0,
                numPackets,
                alive,
                Instant.now()
        );
    }

    private String toJson(RouterMetric metric) {
        return """
                {"deviceId":"%s","temperature":%.2f,"numPackets":%d,"alive":%s,"timestamp":"%s"}
                """.formatted(
                metric.deviceId(),
                metric.temperature(),
                metric.numPackets(),
                metric.alive(),
                metric.timestamp()
        ).trim();
    }
}