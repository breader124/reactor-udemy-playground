package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.MovieInfo;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieServiceTest {

    // in a real world most probably other services would be replaced with later injected mocks
    private final MovieService movieService = new MovieService(new MovieInfoService(), new ReviewService());

    @Test
    public void shouldReturnMovieWhenMovieIdHasBeenPassed() {
        // given
        int movieId = 1001;

        // when
        Mono<Movie> movieMono = movieService.getMovieById(movieId);

        // then
        StepVerifier.create(movieMono)
                .assertNext(m -> {
                    var info = m.getMovie();
                    assertEquals(movieId, info.getMovieInfoId());
                    assertEquals("Batman Begins", info.getName());

                    var reviews = m.getReviewList();
                    assertEquals(2, reviews.size());
                });
    }

    @Test
    public void shouldReturnMovieWhenMovieIdHasBeenPassed_withFlatMapInside() {
        // given
        int movieId = 1001;

        // when
        Mono<Movie> movieMono = movieService.getMovieByIdUsingFlatMap(movieId);

        // then
        StepVerifier.create(movieMono)
                .assertNext(m -> {
                    var info = m.getMovie();
                    assertEquals(movieId, info.getMovieInfoId());
                    assertEquals("Batman Begins", info.getName());

                    var reviews = m.getReviewList();
                    assertEquals(2, reviews.size());
                });
    }
}