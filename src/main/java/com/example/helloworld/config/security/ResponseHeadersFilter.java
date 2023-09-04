package com.example.helloworld.config.security;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.EXPIRES;

import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
@Order(HIGHEST_PRECEDENCE)
public class ResponseHeadersFilter implements WebFilter {

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final var headers = exchange.getResponse().getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-XSS-Protection", "0");
        headers.set("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        headers.set("X-Frame-Options", "deny");
        headers.set("X-Content-Type-Options", "nosniff");
        headers.set("Content-Security-Policy", "default-src 'self'; frame-ancestors 'none';");
        headers.set(CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate");
        headers.setPragma("no-cache");
        headers.set(EXPIRES, "0");

        return chain.filter(exchange);
    }
}
