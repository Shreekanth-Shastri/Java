package com.sample.springreactive;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootTest
class SpringReactiveApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testMono() {
		log.debug("this is a test");
		Mono<?> testMono = Mono.just("spring-reactive")
				.then(Mono.error(new RuntimeException("Exception occured in mono")))
				.log();
		testMono.subscribe(System.out::println, (exeption) -> System.out.println(exeption.getMessage()));
		assertTrue(true);
	}

	@Test
	void testFlux() {
		Flux<?> testFlux = Flux.just("spring", "reactive", "microservice", "java")
				.concatWithValues("AWS")
				.concatWith(Mono.error(new RuntimeException("Exception occured in flux")))
				.concatWithValues("new value")
				.log();
		testFlux.subscribe(System.out::println, (exeption) -> System.out.println(exeption.getMessage()));
		assertTrue(true);
	}

}
