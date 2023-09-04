package com.example.helloworld.services;

import org.springframework.stereotype.Service;

import com.example.helloworld.models.Message;

import reactor.core.publisher.Mono;

@Service
public record MessageService() {

    public Mono<Message> makePublicMessage() {
        final var text = "This is a public message.";

        return Mono.just(text).map(Message::from);
    }

    public Mono<Message> makeProtectedMessage() {
        final var text = "This is a protected message.";

        return Mono.just(text).map(Message::from);
    }

    public Mono<Message> makeAdminMessage() {
        final var text = "This is an admin message.";

        return Mono.just(text).map(Message::from);
    }
}
