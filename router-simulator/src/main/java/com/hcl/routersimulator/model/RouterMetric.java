package com.hcl.routersimulator.model;

import java.time.Instant;

public record RouterMetric(
        String deviceId,
        double temperature,
        long numPackets,
        boolean alive,
        Instant timestamp
) {
}