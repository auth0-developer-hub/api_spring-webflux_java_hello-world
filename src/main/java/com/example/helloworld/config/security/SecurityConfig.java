package com.example.helloworld.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import com.example.helloworld.config.GlobalErrorHandler;
import com.example.helloworld.config.Paths;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final GlobalErrorHandler errorHandler;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        final var messages = Paths.apiPath().messagesPath();
        final var secureMatchers = ServerWebExchangeMatchers.pathMatchers(
                messages.protectedPath().build(),
                messages.adminPath().build()
        );

        return http
                .authorizeExchange(authz -> authz
                        .matchers(secureMatchers).authenticated()
                        .anyExchange().permitAll())
                .cors(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .authenticationEntryPoint(errorHandler::handleAuthenticationError)
                        .jwt(Customizer.withDefaults()))
                .build();
    }
}
