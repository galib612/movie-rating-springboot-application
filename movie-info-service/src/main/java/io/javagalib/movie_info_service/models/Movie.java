package io.javagalib.movie_info_service.models;

public class Movie {
    private String movieId;
    private String name;

    private String overview;

    public Movie(String movieId, String name, String overview) {
        this.movieId = movieId;
        this.name = name;
        this.overview = overview;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
