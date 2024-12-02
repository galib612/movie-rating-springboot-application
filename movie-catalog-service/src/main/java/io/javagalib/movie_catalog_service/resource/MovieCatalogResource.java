package io.javagalib.movie_catalog_service.resource;


import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.javagalib.movie_catalog_service.models.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {


    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private BulkheadRegistry bulkheadRegistry;

    @Autowired
    private ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private TimeLimiterRegistry timeLimiterRegistry;


    @RequestMapping("/{userId}")
    @CircuitBreaker(name = "myService", fallbackMethod = "getFallbackCatalog")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){


        // Get all rated movie IDs
        UserRating ratings = webClientBuilder.build().get()
                .uri("http://rating-data-service/ratingsdata/users/" + userId)
                .retrieve()
                .bodyToMono(UserRating.class)
                .block();

        return ratings.getUserRating().stream().map(rating -> {
            Movie movie = webClientBuilder.build().get()
                    .uri("http://movie-info-service/movie/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();
            return new CatalogItem(movie.getName(), movie.getOverview(), rating.getRating());
        }).collect(Collectors.toList());
    }

    public List<CatalogItem> getFallbackCatalog(String userId, Exception e) {
        return Arrays.asList(new CatalogItem("No Movie", "", 0));
    }

}
