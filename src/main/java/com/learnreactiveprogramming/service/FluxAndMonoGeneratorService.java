package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    public static Mono<String> nameMono() {
        return Mono.just("Lynx").log();
    }

    public static Mono<String> nameMonoNoMoreThanNLetters(int maxLen) {
        return Mono.just("Jacek")
                .filter(s -> s.length() <= maxLen)
                .map(String::toLowerCase)
                .log();
    }

    public static void main(String[] args) {
        System.out.println("Reactive Java rocks!");
    }

}
