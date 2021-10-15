package com.learnreactiveprogramming;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

public class ColdAndHotPublisherTest {

    // difference between cold and hot streams is that whenever you subscribe to cold stream you'll receive the same
    // event (using Reactor terminology), in case of hot streams, what you receive depends on when you subscribe to
    // the publisher

    @Test
    public void hotPublisherTest() {
        var flux = Flux
                .range(1, 100)
                .delayElements(Duration.ofSeconds(1));

        // there are 3 methods, which can be applied to the object of ConnectableFlux type: connect / autoConnect and
        // refCount, this type refCount has been used giving the effect of starting publishing events after 2 publishers
        // subscribed
        var connectableFlux = flux.publish().refCount(2);

        delay(3000);
        var sub1 = connectableFlux.subscribe(item -> System.out.println("1st sub: " + item));
        delay(3000);
        var sub2 = connectableFlux.subscribe(item -> System.out.println("2nd sub: " + item));
        delay(3000);
        sub1.dispose();
        sub2.dispose();
        delay(3000);
    }

}
