package com.hcl.simplenms.repository;

import com.hcl.simplenms.entity.DeviceStatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceStatusRepository extends JpaRepository<DeviceStatusEntity, String> {

    Page<DeviceStatusEntity> findByDeviceIdIn(List<String> deviceIds, Pageable pageable);
}