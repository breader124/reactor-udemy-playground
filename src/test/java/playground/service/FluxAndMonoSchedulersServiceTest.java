package playground.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;


public class FluxAndMonoSchedulersServiceTest {

    private final FluxAndMonoSchedulersService service = new FluxAndMonoSchedulersService();

    @Test
    public void explorePublishOn() {
        // when
        var flux = service.explorePublishOn();

        // then
        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void exploreParallelWithRunOn() {
        // when
        var flux = service.exploreParallelWithRunOn();

        // then
        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void exploreParallelWithFlatMap() {
        // when
        var flux = service.exploreParallelWithRunOn();

        // then
        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void exploreParallelWithFlatMapSequential() {
        // when
        var flux = service.exploreParallelWithRunOn();

        // then
        StepVerifier.create(flux)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

}
