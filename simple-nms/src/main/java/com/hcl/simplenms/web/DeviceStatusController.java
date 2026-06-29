package com.hcl.simplenms.web;

import com.hcl.simplenms.entity.DeviceStatusEntity;
import com.hcl.simplenms.repository.DeviceStatusRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DeviceStatusController {

    private final DeviceStatusRepository repository;

    public DeviceStatusController(DeviceStatusRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/getDeviceStatus")
    public DeviceStatusEntity getDeviceStatus(@RequestBody Map<String, String> request) {
        String deviceId = request.get("deviceId");

        return repository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found: " + deviceId));
    }

    @PostMapping("/getDeviceStatusMultiple")
    public Page<DeviceStatusEntity> getDeviceStatusMultiple(
            @RequestBody Map<String, List<String>> request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<String> deviceIds = request.get("deviceIds");

        if (deviceIds == null || deviceIds.isEmpty()) {
            return repository.findAll(PageRequest.of(page, size));
        }

        return repository.findByDeviceIdIn(deviceIds, PageRequest.of(page, size));
    }
}