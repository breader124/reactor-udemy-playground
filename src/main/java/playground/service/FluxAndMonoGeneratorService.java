package playground.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class FluxAndMonoGeneratorService {

    public static Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("Jacek", "Jan", "Adrian", "Agata")).log();
    }

    public static Flux<String> namesFluxNoMoreThanNLetters(int maxLen) {
        return Flux.just("Jacek", "Jan", "Adrian", "Agata")
                .filter(s -> s.length() <= maxLen)
                .map(String::toUpperCase)
                .log();
    }

    public static Flux<String> namesFluxLetterByLetterSync() {
        return Flux.just("Jacek", "Jan", "Adrian", "Agata")
                .concatMap(s -> Flux.fromArray(s.split("")))
                .log();
    }

    public static Flux<String> namesFluxLetterByLetterAsync() {
        return Flux.just("Jacek", "Jan", "Adrian", "Agata")
                .flatMap(s -> Flux.fromArray(s.split("")))
                .log();
    }

    // it just replaces value with another one and continue creating Flux object applying next transformations
    public static Flux<String> namesFluxDefaultIfEmpty(int maxLen) {
        return Flux.just("Jacek", "Jan", "Adrian", "Agata")
                .filter(s -> s.length() <= maxLen)
                .defaultIfEmpty("Bogdan")
                .map(String::toUpperCase)
                .log();
    }

    // it replaces currently built Flux with another one applying defined operations on top of the ones it currently contains
    public static Flux<String> namesFluxSwitchIfEmpty(int maxLen) {
        var alternativeFlux = Flux.just("Bogdan")
                .map(s -> s += " and Adrian")
                .log();
        return Flux.just("Jacek", "Jan", "Adrian", "Agata")
                .filter(s -> s.length() <= maxLen)
                .switchIfEmpty(alternativeFlux)
                .map(String::toUpperCase)
                .log();
    }

    public static Flux<String> exploreConcatWith() {
        var firstFlux = Flux.just("A", "B", "C");
        var secondFlux = Flux.just("D", "E", "F");
        return firstFlux.concatWith(secondFlux);
    }

    public static Flux<Integer> exploreZipWith() {
        var firstStream = IntStream.range(0, 2);
        var firstFlux = Flux.fromStream(firstStream.boxed());
        var secondStream = IntStream.range(3, 5);
        var secondFlux = Flux.fromStream(secondStream.boxed());
        return firstFlux.zipWith(secondFlux, Integer::sum);
    }

    public static Flux<String> exploreOnErrorResume() {
        return Flux.just("Jacek", "Jan", "Adrian", "Agata")
                .map(s -> {
                    if (s.length() <= 3) {
                        throw new RuntimeException();
                    }
                    return s;
                })
                .onErrorResume((exc) -> Flux.just("Agatha", "Hadrian"));
    }

    public static Flux<String> exploreOnErrorReturn() {
        return Flux.just("Jacek", "Jan", "Adrian", "Agata")
                .map(s -> {
                    if (s.length() <= 3) {
                        throw new RuntimeException();
                    }
                    return s;
                })
                .onErrorReturn("Bradley");
    }

    public static Flux<String> exploreOnErrorContinue() {
        return Flux.just("Jacek", "Jan", "Adrian", "Agata")
                .map(s -> {
                    if (s.length() <= 3) {
                        throw new RuntimeException();
                    }
                    return s;
                })
                .onErrorContinue((exc, value) -> log.info("{} has been filtered out. Please do not resist", value));
    }

    public static Mono<String> nameMono() {
        return Mono.just("Lynx").log();
    }

    public static Mono<String> nameMonoNoMoreThanNLetters(int maxLen) {
        return Mono.just("Jacek")
                .filter(s -> s.length() <= maxLen)
                .map(String::toLowerCase)
                .log();
    }

    public static Mono<String> namesMonoMapFilter(int stringLength) {
        return Mono.just("Hadrian")
                .filter(s -> s.length() < stringLength)
                .defaultIfEmpty("I'm gonna show you default value")
                .log();
    }

    public static Mono<String> namesMonoMapFilterSwitchIfEmpty(int stringLength) {
        var alternativeMono = Mono.just("I'm gonna show you default value");
        return Mono.just("Hadrian")
                .filter(s -> s.length() < stringLength)
                .switchIfEmpty(alternativeMono)
                .log();
    }

    public static Flux<String> exploreConcatWithMono() {
        var firstMono = Mono.just("A");
        var secondMono = Mono.just("B");

        return firstMono.concatWith(secondMono);
    }

    public static Mono<Integer> exploreMonoZipWith() {
        var firstMono = Mono.just(11);
        var secondMono = Mono.just(2);
        return firstMono.zipWith(secondMono, Integer::divideUnsigned);
    }

    public static Mono<String> exploreMonoOnErrorContinue(String input) {
        return Mono.just(input)
                .map(s -> {
                    if ("abc".equals(input)) {
                        throw new RuntimeException("Input is different than 'abc'");
                    }
                    return s;
                })
                .onErrorContinue((exc, inp) -> {
                    log.error("Following exception occurred", exc);
                    log.info("Following input has been passed: {}", inp);
                });
    }

    public static void main(String[] args) {
        System.out.println("Reactive Java rocks!");
    }

}
