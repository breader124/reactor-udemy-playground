package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.MovieInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieInfoService movieInfoService;

    @Mock
    private ReviewService reviewService;

    @Test
    public void shouldReturnMovieWhenMovieIdHasBeenPassed() {
        // given
        int movieId = 1001;

        // when
        when(movieInfoService.retrieveMovieInfoMonoUsingId(movieId)).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(movieId)).thenCallRealMethod();
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
        when(movieInfoService.retrieveMovieInfoMonoUsingId(movieId)).thenCallRealMethod();
        lenient().when(reviewService.retrieveReviewsFlux(movieId)).thenCallRealMethod();
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