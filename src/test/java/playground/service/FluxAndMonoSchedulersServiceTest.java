package playground.service;

import org.junit.jupiter.api.Test;
import playground.service.FluxAndMonoGeneratorService;
import reactor.test.StepVerifier;


public class FluxAndMonoSchedulersServiceTest {

    @Test
    private void namesFlux() {
        // when
        var namesFlux = FluxAndMonoGeneratorService.namesFlux();

        // then
        StepVerifier.create(namesFlux)
                .expectNext("Jacek", "Jan", "Adrian", "Agata")
                .verifyComplete();
    }

}
