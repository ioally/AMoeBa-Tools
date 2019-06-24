package com.ioally.amoeba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AmoebaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmoebaApplication.class, args);
    }
}
