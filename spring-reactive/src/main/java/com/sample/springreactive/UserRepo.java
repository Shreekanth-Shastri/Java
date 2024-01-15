package com.sample.springreactive;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@Repository
public class UserRepo {

    private static void sleepExecution(int i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("exception: {}, {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

    public List<User> getUsers() {
        return IntStream.rangeClosed(1, 10)
                .peek(UserRepo::sleepExecution) /* For simulating getting data from database */
                .peek(i -> System.out.println("counter: " + i))
                .mapToObj(i -> new User(i, "firstName" + i, "lastName" + i, "test@test.com" + i,
                        "987-654-3210", "123 main Street, City, StateCode, 54321"))
                .collect(Collectors.toList());
    }

    public Flux<User> getUsersFlux() {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(i -> System.out.println("flux counter: " + i))
                .map(i -> new User(i, "firstName" + i, "lastName" + i, "test@test.com" + i,
                        "987-654-3210", "123 main Street, City, StateCode, 54321"));
    }

    public Flux<User> getUsersForHandler() {
        return Flux.range(1, 10)
                .doOnNext(i -> System.out.println("flux counter: " + i))
                .map(i -> new User(i, "firstName" + i, "lastName" + i, "test@test.com" + i,
                        "987-654-3210", "123 main Street, City, StateCode, 54321"));
    }
}
