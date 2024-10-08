package io.javagalib.movie_catalog_service.resource;


import io.javagalib.movie_catalog_service.models.CatalogItem;
import io.javagalib.movie_catalog_service.models.Movie;
import io.javagalib.movie_catalog_service.models.Rating;
import io.javagalib.movie_catalog_service.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private  RestTemplate restTemplate;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){


        // Get all rated movie IDs
        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {
                    // For each movie ID, call movie info service and get details
                    Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
                    // put them all together
                    return new CatalogItem(movie.getName(),"Desc", rating.getRating());
        })
                .collect(Collectors.toList());

    }
}
