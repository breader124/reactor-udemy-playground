package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class MovieService {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;
    private RevenueService revenueService;

    public Flux<Movie> getAllMovies() {
        var movieInfoFlux = movieInfoService.retrieveMoviesFlux();
        // flatMap has been used here because we're unsure how much time computation inside can take, but wouldn't
        // like to block execution by forcing sync execution
        return movieInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviews = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
                    return reviews.map(review -> new Movie(movieInfo, review));
                })
                .onErrorMap((exc) -> {
                    log.error("Exception has been thrown", exc);
                    throw new MovieException(exc);
                });
    }

    public Mono<Movie> getMovieById(int movieId) {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        // there's need to be careful while using collectList like in line below, it assumes that all elements
        // of transformed Flux must appear first and thus async computations are ruined
        var reviewsMono = reviewService.retrieveReviewsFlux(movieId).collectList();
        return movieInfoMono.zipWith(reviewsMono, Movie::new);
    }

    public Mono<Movie> getMovieWithRevenueWithById(int movieId) {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        // once again, line below needs to be annotated as dangerous, because it forces main thread to wait to
        // all events from Flux to appear, to be then converted to Mono of List, after all it's requirement
        // to zip with complete list, so there's no make any actions to improve performance in such case
        var reviewsMono = reviewService.retrieveReviewsFlux(movieId).collectList();
        var revenueMono = Mono
                .fromCallable(() -> revenueService.getRevenue((long) movieId))
                .subscribeOn(Schedulers.parallel());
        // subscribeOn above has been used to make computations parallel, I don't want main thread to wait for
        // I/O operations to complete

        return movieInfoMono
                .zipWith(reviewsMono, Movie::new)
                .zipWith(revenueMono, (movie, revenue) -> {
                    movie.setRevenue(revenue);
                    return movie;
                });
    }

    public Mono<Movie> getMovieByIdUsingFlatMap(int movieId) {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        return movieInfoMono.flatMap(movieInfo -> {
            var reviewsMono = reviewService.retrieveReviewsFlux(movieId).collectList();
            return reviewsMono.map(reviews -> new Movie(movieInfo, reviews));
        });
    }

}
