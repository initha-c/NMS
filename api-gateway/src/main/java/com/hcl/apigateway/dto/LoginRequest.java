package com.hcl.apigateway.dto;

public record LoginRequest(
        String username,
        String password
) {
}