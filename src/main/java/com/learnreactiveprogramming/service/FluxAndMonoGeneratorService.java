package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public static Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("Jacek", "Jan", "Adrian", "Agata")).log();
    }

    public static Mono<String> nameMono() {
        return Mono.just("Lynx").log();
    }

    public static void main(String[] args) {
        System.out.println("~~~~~~~~~~~~~~~~~~~ Flux now ~~~~~~~~~~~~~~~~~~~");
        namesFlux().subscribe(System.out::println);

        System.out.println("\n~~~~~~~~~~~~~~~~~~~ Mono now ~~~~~~~~~~~~~~~~~~~");
        nameMono().subscribe(System.out::println);
    }

}
