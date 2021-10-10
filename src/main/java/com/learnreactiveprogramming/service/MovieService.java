package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class MovieService {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;

    public Flux<Movie> getAllMovies() {
        var movieInfoFlux = movieInfoService.retrieveMoviesFlux();
        // flatMap has been used here because we're unsure how much time computation inside can take, but wouldn't
        // like to block execution by forcing sync execution
        return movieInfoFlux.flatMap(movieInfo -> {
            Mono<List<Review>> reviews = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
            return reviews.map(review -> new Movie(movieInfo, review));
        });
    }

    public Mono<Movie> getMovieById(int movieId) {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        // there's need to be careful while using collectList like in line below, it assumes that all elements
        // of transformed Flux must appear first and thus async computations are ruined
        var reviewsMono = reviewService.retrieveReviewsFlux(movieId).collectList();
        return movieInfoMono.zipWith(reviewsMono, Movie::new);
    }

    public Mono<Movie> getMovieByIdUsingFlatMap(int movieId) {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        return movieInfoMono.flatMap(movieInfo -> {
            var reviewsMono = reviewService.retrieveReviewsFlux(movieId).collectList();
            return reviewsMono.map(reviews -> new Movie(movieInfo, reviews));
        });
    }

}
