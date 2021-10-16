package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieInfoServiceTest {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    private final MovieInfoService movieInfoService = new MovieInfoService(webClient);

    @Test
    public void retrieveAllMovieInfo() {
        // when
        var movieInfoFlux = movieInfoService.retrieveAllMovieInfoViaRestClient();

        // then
        StepVerifier.create(movieInfoFlux)
                .expectNextCount(7)
                .verifyComplete();
    }

    @Test
    public void retrieveMovieInfoById() {
        // when
        var movieInfoMono = movieInfoService.retrieveMovieInfoByIdViaRestClient(1L);

        // then
        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertEquals(1L, movieInfo.getMovieInfoId());
                    assertEquals("Batman Begins", movieInfo.getName());
                    assertEquals(2005, movieInfo.getYear());
                })
                .verifyComplete();
    }

}
