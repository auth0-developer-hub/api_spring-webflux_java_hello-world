package com.example.helloworld.config;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.example.helloworld.models.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.joselion.maybe.Maybe;

import reactor.core.publisher.Mono;

@Order(-2)
@Component
public record GlobalErrorHandler(ObjectMapper mapper) implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable error) {
        final var response = exchange.getResponse();
        final var bufferFactory = response.bufferFactory();

        if (error instanceof ResponseStatusException statusError) {
            final var status = statusError.getStatusCode();
            response.setStatusCode(status);

            if (status.equals(HttpStatus.NOT_FOUND)) {
                final var body = this.makeBodyBytes("Not Found")
                        .map(bufferFactory::wrap);

                return response.writeWith(body);
            }
        }

        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        return response.writeWith(
                this.makeBodyBytes(error.getMessage())
                        .map(bufferFactory::wrap));
    }

    public Mono<Void> handleAuthenticationError(final ServerWebExchange exchange, final AuthenticationException error) {
        final var response = exchange.getResponse();
        final var message = "Unauthorized. %s".formatted(error.getMessage());
        final var body = this.makeBodyBytes(message)
                .map(response.bufferFactory()::wrap);

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(body);
    }

    public Mono<Void> handleAccessDenied(
            final ServerWebExchange exchange,
            final AccessDeniedException error // NOSONAR
    ) {
        final var response = exchange.getResponse();
        final var body = this.makeBodyBytes("Permission denied")
                .map(response.bufferFactory()::wrap);

        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.writeWith(body);
    }

    private Mono<byte[]> makeBodyBytes(final String message) {
        return Mono.create(sink -> Maybe.just(message)
                .map(ErrorMessage::from)
                .resolve(mapper::writeValueAsBytes)
                .doOnSuccess(sink::success)
                .doOnError(sink::error));
    }
}
