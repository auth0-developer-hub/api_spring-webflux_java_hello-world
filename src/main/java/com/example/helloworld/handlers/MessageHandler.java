package com.example.helloworld.handlers;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.helloworld.services.MessageService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MessageHandler {

    private final MessageService messageService;

    @Cacheable
    public Mono<ServerResponse> getPublic(final ServerRequest request) {
        return messageService.makePublicMessage()
                .flatMap(ok()::bodyValue);
    }

    @Cacheable
    public Mono<ServerResponse> getProtected(final ServerRequest request) {
        return messageService.makeProtectedMessage()
                .flatMap(ok()::bodyValue);
    }

    @Cacheable
    public Mono<ServerResponse> getAdmin(final ServerRequest request) {
        return messageService.makeAdminMessage()
                .flatMap(ok()::bodyValue);
    }
}
