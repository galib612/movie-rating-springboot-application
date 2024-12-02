package io.javagalib.movie_catalog_service;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootApplication
//@EnableCircuitBreaker // if you're using Spring Boot 2.x or later with Resilience4j, this might not be necessary because Spring Boot auto-configures it.
public class MovieCatalogServiceApplication {

	@Bean
	public BulkheadRegistry getBulkheadRegistry() {
		return BulkheadRegistry.ofDefaults(); // Default configuration
	}

	@Bean
	public ThreadPoolBulkheadRegistry getThreadPoolBulkheadRegistry() {
		return ThreadPoolBulkheadRegistry.ofDefaults(); // Default configuration
	}

	@Bean
	public CircuitBreakerRegistry getCircuitBreakerRegistry() {
		return CircuitBreakerRegistry.ofDefaults(); // Default configuration
	}

	@Bean
	public TimeLimiterRegistry getTimeLimiterRegistry() {
		return TimeLimiterRegistry.ofDefaults(); // Default configuration
	}

	@Bean
	@LoadBalanced
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}


	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

}


/*
	Circuit breaker pattern

	1. Detect something wrong
	2. Take temporary steps to avoid the situation getting worse
	3. Deactivate the problem component so that it doesn't affect downstream components


	Circuit Breaker --> it's basic function is to interrupt current flow after a fault is detected

	When Does the circuit trip?

	>>> Last n requests to consider for decisions: 5
	>>> How many of those should fail: 3
	>>> Timeout: 2

	When does the circuit un-trip??

	>>> How long after a circuit trip to try again(Sleep Window): 10s

	https://github.com/Netflix/Hystrix/wiki/Configuration

	Circuit Tripped:; now what?

	>>> We need a fallback
		>>> Throw an error
		>>> Return a fallback "default" response
		>>> Save previous responses (cache) and use that when possible

	>>> Why circuit breakers
		>>> Failing fast
		>>> Fallback functionality
		>>> Automatic Recovery


	https://github.com/Netflix/Hystrix/wiki/Configuration

	Hystrix is framework for Circuit breaker

	>>> Open Source library originally created by netflix
	>>> implements circuit breaker pattern so you don't have to
	>>> Give it the configuration params and it does the work
	>>> works well with Spring Boot

	Adding Hystrix to a Spring Boot microservices
	>>> Add the Maven spring-cloud-starter-netflix-hystrix dependency
    		<groupId>com.netflix.hystrix</groupId>
    		<artifactId>hystrix-core</artifactId>
	>>> Add @EnableCircuitBreaker to the applications clas
	>>> Add @HystrixCommand to methods that need circuit breakers
	>>> Configure Hystrix behaviour


	https://www.youtube.com/playlist?list=PL0zysOflRCelb2Y4WOVckFC6B050BzV0D

 */