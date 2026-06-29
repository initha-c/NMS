CREATE TABLE IF NOT EXISTS device_status (
    device_id VARCHAR(255) PRIMARY KEY,
    temperature DOUBLE,
    num_packets BIGINT,
    alive BOOLEAN,
    metric_timestamp TIMESTAMP,
    updated_at TIMESTAMP
);