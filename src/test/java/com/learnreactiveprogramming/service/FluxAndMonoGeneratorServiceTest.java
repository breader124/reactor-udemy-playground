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
    public void testNamesFluxNoMoreThan3LettersAndUpperCase() {
        // when
        var namesFlux = FluxAndMonoGeneratorService.namesFluxNoMoreThanNLetters(3);

        // then
        StepVerifier.create(namesFlux)
                .expectNext("JAN")
                .verifyComplete();
    }

    @Test
    public void testNamesFluxDefaultIfEmpty() {
        // when
        var namesFlux = FluxAndMonoGeneratorService.namesFluxDefaultIfEmpty(2);

        // then
        StepVerifier.create(namesFlux)
                .expectNext("BOGDAN")
                .verifyComplete();
    }

    @Test
    public void testNamesFluxSwitchIfEmpty() {
        // when
        var namesFlux = FluxAndMonoGeneratorService.namesFluxSwitchIfEmpty(2);

        // then
        StepVerifier.create(namesFlux)
                .expectNext("BOGDAN AND ADRIAN")
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

    @Test
    public void testNameMonoNoMoreThan3LettersAndLowerCase() {
        // when
        var nameMono = FluxAndMonoGeneratorService.nameMonoNoMoreThanNLetters(3);

        // then
        StepVerifier.create(nameMono)
                .verifyComplete();
    }

}
