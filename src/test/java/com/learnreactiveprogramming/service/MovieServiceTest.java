package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.MovieInfo;
import com.learnreactiveprogramming.exception.MovieException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    private final int movieId = 1001;

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieInfoService movieInfoService;

    @Mock
    private ReviewService reviewService;

    @Test
    public void shouldReturnMovieWhenMovieIdHasBeenPassed() {
        // given
        when(movieInfoService.retrieveMovieInfoMonoUsingId(movieId)).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(movieId)).thenCallRealMethod();

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
        when(movieInfoService.retrieveMovieInfoMonoUsingId(movieId)).thenCallRealMethod();
        lenient().when(reviewService.retrieveReviewsFlux(movieId)).thenCallRealMethod();

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

    @Test
    public void shouldReturnAllMoviesWhenEachServiceRespondedWithoutErrors() {
        // given
        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong())).thenCallRealMethod();

        // when
        Flux<Movie> movieFlux = movieService.getAllMovies();

        // then
        StepVerifier.create(movieFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void shouldThrowADomainExceptionWhenReviewServiceRespondedWithAnError() {
        // given
        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong())).thenThrow(new RuntimeException());

        // when
        Flux<Movie> movieFlux = movieService.getAllMovies();

        // then
        StepVerifier.create(movieFlux)
                .expectError(MovieException.class)
                .verify();
    }
}