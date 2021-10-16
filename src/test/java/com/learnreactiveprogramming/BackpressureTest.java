package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class BackpressureTest {

    @Test
    public void backpressureMechanismTryout() {
        Flux<Integer> numbersRange = Flux.range(0, 100);
        numbersRange
                .log()
                .onBackpressureBuffer(2, (elem) -> log.info("Last buffered elem is: {}", elem))
                .subscribe(new BaseSubscriber<>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                        log.info("Just request next 2 elements");
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        if (value < 10) {
                            if (value % 2 != 0) {
                                request(2);
                                log.info("Just request next 2 elements");
                            }
                        }
                    }
                });
    }

}
