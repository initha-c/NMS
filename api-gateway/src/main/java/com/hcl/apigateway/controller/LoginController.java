package com.hcl.apigateway.controller;

import com.hcl.apigateway.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
public class LoginController {

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";
    private static final String DEMO_TOKEN = "valid-demo-token";

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {

        if (VALID_USERNAME.equals(request.username()) &&
                VALID_PASSWORD.equals(request.password())) {

            return ResponseEntity.ok(Map.of(
                    "token", DEMO_TOKEN,
                    "tokenType", "Bearer",
                    "issuedAt", Instant.now().toString(),
                    "message", "Login successful"
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "message", "Invalid username or password"
        ));
    }
}