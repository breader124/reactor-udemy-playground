package playground.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
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

    public ParallelFlux<String> exploreParallelWithRunOn() {
        return Flux.fromIterable(namesList)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(this::upperCase)
                .log();
    }

    public Flux<String> exploreParallelWithFlatMap() {
        return Flux.fromIterable(namesList)
                .flatMap((name) -> Mono
                        .just(name)
                        .map(this::upperCase)
//                      line below is not required in current case, it's there only as a good practice and reminder
//                      that it should be present whenever we don't want to block execution of the thread, which
//                      started execution of stream processing, e.g. 'main' one
                        .subscribeOn(Schedulers.parallel())
                )
                .log();
    }

    public Flux<String> exploreParallelWithFlatMapSequential() {
        return Flux.fromIterable(namesList)
                .flatMapSequential((name) -> Mono
                        .just(name)
                        .map(this::upperCase)
//                      line below is not required in current case, it's there only as a good practice and reminder
//                      that it should be present whenever we don't want to block execution of the thread, which
//                      started execution of stream processing, e.g. 'main' one
                        .subscribeOn(Schedulers.parallel())
                )
                .log();
    }

    private String upperCase(String name) {
        delay(1000);
        return name.toUpperCase();
    }

}
