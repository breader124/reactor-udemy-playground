package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    private final ReviewService reviewService = new ReviewService(webClient);

    @Test
    public void retrieveReviewsViaRestCall() {
        // when
        var flux = reviewService.retrieveReviewsFluxViaRestClient(1L);

        // then
        StepVerifier.create(flux)
                .assertNext(review -> {
                    assertEquals(1L, review.getReviewId());
                    assertEquals(1L, review.getMovieInfoId());
                    assertEquals(8.2, review.getRating());
                    assertEquals("Nolan is the real superhero", review.getComment());
                })
                .verifyComplete();
    }

}
