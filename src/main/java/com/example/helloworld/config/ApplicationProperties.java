package com.example.helloworld.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public record ApplicationProperties(String clientOriginUrl) {
}
