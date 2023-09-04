package com.example.helloworld.config;

import java.util.Objects;

import org.springframework.lang.Nullable;
import org.springframework.web.util.DefaultUriBuilderFactory;

public sealed interface Paths {

    String segment();

    @Nullable
    Paths root();

    default String build() {
        final var factory = new DefaultUriBuilderFactory();
        final var rootPath = root();
        final var prevPath = Objects.nonNull(rootPath)
                ? rootPath.build()
                : "";
        final var uriBuilder = factory.uriString(prevPath);

        return uriBuilder.path(segment())
                .build()
                .getPath();
    }

    static ApiPaths apiPath() {
        return new ApiPaths("/api", null);
    }

    record ApiPaths(String segment, Paths root) implements Paths {

        public MessagesPaths messagesPath() {
            return new MessagesPaths("/messages", Paths.apiPath());
        }

        public record MessagesPaths(String segment, Paths root) implements Paths {

            public MessagesEndpoint publicPath() {
                return new MessagesEndpoint("/public");
            }

            public MessagesEndpoint protectedPath() {
                return new MessagesEndpoint("/protected");
            }

            public MessagesEndpoint adminPath() {
                return new MessagesEndpoint("/admin");
            }

            public record MessagesEndpoint(String segment, Paths root) implements Paths {

                public MessagesEndpoint(final String segment) {
                    this(segment, Paths.apiPath().messagesPath());
                }
            }
        }
    }
}
