package com.hcl.simplenms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "device_status")
public class DeviceStatusEntity {

    @Id
    private String deviceId;

    private double temperature;

    private long numPackets;

    private boolean alive;

    private Instant metricTimestamp;

    private Instant updatedAt;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public long getNumPackets() {
        return numPackets;
    }

    public void setNumPackets(long numPackets) {
        this.numPackets = numPackets;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Instant getMetricTimestamp() {
        return metricTimestamp;
    }

    public void setMetricTimestamp(Instant metricTimestamp) {
        this.metricTimestamp = metricTimestamp;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}