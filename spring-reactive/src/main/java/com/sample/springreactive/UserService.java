package com.sample.springreactive;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo userRepository;

    public List<User> getAllUsers() {
        long startTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        List<User> userList = userRepository.getUsers();
        long endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        log.debug("time taken for execution: {}", endTime - startTime);
        return userList;
    }

    public Flux<User> getAllUsersFlux() {
        long startTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        Flux<User> userList = userRepository.getUsersFlux();
        long endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        log.debug("time taken for execution in flux: {}", endTime - startTime);
        return userList;
    }


}
