package playground.service;

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
    public void testExploreFluxConcatWith() {
        // when
        var concatenatedFlux = FluxAndMonoGeneratorService.exploreConcatWith();

        // then
        StepVerifier.create(concatenatedFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void testZipWith() {
        // when
        var zippedFlux = FluxAndMonoGeneratorService.exploreZipWith();

        // then
        StepVerifier.create(zippedFlux)
                .expectNext(3, 5)
                .verifyComplete();
    }

    @Test
    public void testOnErrorResume() {
        // when
        var flux = FluxAndMonoGeneratorService.exploreOnErrorResume();

        // then
        StepVerifier.create(flux)
                .expectNext("Jacek", "Agatha", "Hadrian")
                .verifyComplete();
    }

    @Test
    public void testOnErrorReturn() {
        // when
        var flux = FluxAndMonoGeneratorService.exploreOnErrorReturn();

        // then
        StepVerifier.create(flux)
                .expectNext("Jacek", "Bradley")
                .verifyComplete();
    }

    @Test
    public void testOnErrorContinue() {
        // when
        var flux = FluxAndMonoGeneratorService.exploreOnErrorContinue();

        // then
        StepVerifier.create(flux)
                .expectNext("Jacek", "Adrian", "Agata")
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

    @Test
    public void testExploreMonoConcatWith() {
        // when
        var concatenatedMonos = FluxAndMonoGeneratorService.exploreConcatWithMono();

        // then
        StepVerifier.create(concatenatedMonos)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    public void testMonoZipWith() {
        // when
        var zippedMonos = FluxAndMonoGeneratorService.exploreMonoZipWith();

        // then
        StepVerifier.create(zippedMonos)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    public void testMonoOnErrorContinueWithInvalidInput() {
        // when
        var endangeredMono = FluxAndMonoGeneratorService.exploreMonoOnErrorContinue("abc");

        // then
        StepVerifier.create(endangeredMono)
                .verifyComplete();
    }

    @Test
    public void testMonoOnErrorContinueWithProperInput() {
        // when
        var endangeredMono = FluxAndMonoGeneratorService.exploreMonoOnErrorContinue("reactor");

        // then
        StepVerifier.create(endangeredMono)
                .expectNext("reactor")
                .verifyComplete();
    }

}
