package com.tismenetski.coronavirustrackerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CoronaVirusTrackerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoronaVirusTrackerAppApplication.class, args);
    }

}
