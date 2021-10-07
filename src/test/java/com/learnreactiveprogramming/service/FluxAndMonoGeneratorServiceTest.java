package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class FluxAndMonoGeneratorServiceTest {

    @Test
    public void namesFlux() {
        // when
        var namesFlux = FluxAndMonoGeneratorService.namesFlux();

        // then
        StepVerifier.create(namesFlux)
                .expectNext("Jacek")
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void nameMono() {
        // when
        var nameMono = FluxAndMonoGeneratorService.nameMono();

        // then
        StepVerifier.create(nameMono)
                .expectNext("Lynx")
                .verifyComplete();
    }

}
