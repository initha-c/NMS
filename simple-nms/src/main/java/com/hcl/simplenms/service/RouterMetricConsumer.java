package com.hcl.simplenms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.simplenms.entity.DeviceStatusEntity;
import com.hcl.simplenms.model.RouterMetric;
import com.hcl.simplenms.repository.DeviceStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RouterMetricConsumer {

    private static final Logger log = LoggerFactory.getLogger(RouterMetricConsumer.class);

    private final ObjectMapper objectMapper;
    private final DeviceStatusRepository repository;

    public RouterMetricConsumer(ObjectMapper objectMapper,
                                DeviceStatusRepository repository) {
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @KafkaListener(
            topics = "${simple-nms.kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String message) {
        try {
            RouterMetric metric = objectMapper.readValue(message, RouterMetric.class);

            DeviceStatusEntity entity = repository
                    .findById(metric.deviceId())
                    .orElseGet(DeviceStatusEntity::new);

            entity.setDeviceId(metric.deviceId());
            entity.setTemperature(metric.temperature());
            entity.setNumPackets(metric.numPackets());
            entity.setAlive(metric.alive());
            entity.setMetricTimestamp(metric.timestamp());
            entity.setUpdatedAt(Instant.now());

            repository.save(entity);

            log.info("Saved router metric into DB. deviceId={}", metric.deviceId());

        } catch (Exception exception) {
            log.error("Failed to process Kafka message: {}", message, exception);
        }
    }
}