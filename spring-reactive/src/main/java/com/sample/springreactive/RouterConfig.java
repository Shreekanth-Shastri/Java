package com.sample.springreactive;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class RouterConfig {

    private final UserHandler userHandler;
    private final UserStreamHandler userStreamHandler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET("router/users", userHandler::loadUsers)
                .GET("router/user-stream", userStreamHandler::loadUsers)
                .build();
    }
}
