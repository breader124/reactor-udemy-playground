package playground.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

public class FluxAndMonoSchedulersService {

    static List<String> namesList = List.of("alex", "ben", "chloe");
    static List<String> namesList1 = List.of("adam", "jill", "jack");

    public Flux<String> explorePublishOn() {
        var namesFlux = Flux.fromIterable(namesList)
                .subscribeOn(Schedulers.parallel()) // it works on entire stream level
                .map(this::upperCase)
                .log();

        var namesFlux1 = Flux.fromIterable(namesList1)
                .publishOn(Schedulers.parallel()) // it works downstream
                .map(this::upperCase)
                .log();

        return namesFlux.mergeWith(namesFlux1);
    }

    private String upperCase(String name) {
        delay(1000);
        return name.toUpperCase();
    }

}
