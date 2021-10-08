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
        System.out.println("~~~~~~~~~~~~~~~~~~~ Flux now ~~~~~~~~~~~~~~~~~~~");
        namesFlux().subscribe(System.out::println);

        System.out.println("\n~~~~~~~~~~~~~~~~~~~ Mono now ~~~~~~~~~~~~~~~~~~~");
        nameMono().subscribe(System.out::println);
    }

}
