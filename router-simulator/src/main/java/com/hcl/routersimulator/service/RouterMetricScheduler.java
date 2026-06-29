package com.hcl.routersimulator.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RouterMetricScheduler {

    private final RouterMetricProducer producer;

    public RouterMetricScheduler(RouterMetricProducer producer) {
        this.producer = producer;
    }

    @Scheduled(fixedDelayString = "${router.simulator.publish-interval-ms}")
    public void publishMetrics() {
        producer.publishMetricsForAllRouters();
    }
}