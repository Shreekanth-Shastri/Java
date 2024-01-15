package com.sample.springreactive;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserHandler {

    private final UserRepo userRepo;

    public Mono<ServerResponse> loadUsers(ServerRequest serverRequest) {
        Flux<User> userList = userRepo.getUsersForHandler();
        return ServerResponse.ok().body(userList, User.class);
    }
}
