package com.hcl.apigateway.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthRateLimitFilter implements WebFilter {

    private static final String VALID_TOKEN = "valid-demo-token";
    private static final int MAX_REQUESTS_PER_MINUTE = 5;

    private final Map<String, RateLimitState> rateLimitMap = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // Allow login and actuator without token
        if (path.equals("/login") || path.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        // Apply security only for API calls
        if (!path.startsWith("/api/")) {
            return chain.filter(exchange);
        }

        String authorizationHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (authorizationHeader == null || !authorizationHeader.equals("Bearer " + VALID_TOKEN)) {
            return writeErrorResponse(
                    exchange.getResponse(),
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized: valid Bearer token is required"
            );
        }

        if (!isAllowed(authorizationHeader)) {
            return writeErrorResponse(
                    exchange.getResponse(),
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Rate limit exceeded. Allowed limit is 5 requests per minute"
            );
        }

        return chain.filter(exchange);
    }

    private boolean isAllowed(String token) {
        long currentMinute = Instant.now().getEpochSecond() / 60;

        RateLimitState state = rateLimitMap.compute(token, (key, existingState) -> {
            if (existingState == null || existingState.windowMinute != currentMinute) {
                return new RateLimitState(currentMinute, 1);
            }

            existingState.requestCount++;
            return existingState;
        });

        return state.requestCount <= MAX_REQUESTS_PER_MINUTE;
    }

    private Mono<Void> writeErrorResponse(ServerHttpResponse response,
                                          HttpStatus status,
                                          String message) {
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");

        String body = """
                {
                  "status": %d,
                  "error": "%s",
                  "message": "%s"
                }
                """.formatted(status.value(), status.getReasonPhrase(), message);

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(bytes))
        );
    }

    private static class RateLimitState {
        private final long windowMinute;
        private int requestCount;

        private RateLimitState(long windowMinute, int requestCount) {
            this.windowMinute = windowMinute;
            this.requestCount = requestCount;
        }
    }
}