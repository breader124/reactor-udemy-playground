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

}
