package com.sample.springreactive;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserStreamHandler {

    private final UserRepo userRepo;

    public Mono<ServerResponse> loadUsers(ServerRequest request) {
        Flux<User> userStream = userRepo.getUsersForHandler();
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(userStream, User.class);
    }
}
