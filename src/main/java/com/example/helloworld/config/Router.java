package com.example.helloworld.config;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.helloworld.handlers.MessageHandler;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> apiRouter(final MessageHandler messageHandler) {
        return route()
                .path("/api", () -> route().path("/messages", () -> route()
                        .GET("/public", messageHandler::getPublic)
                        .GET("/protected", messageHandler::getProtected)
                        .GET("/admin", messageHandler::getAdmin)
                        .build())
                        .build())
                .build();
    }
}
