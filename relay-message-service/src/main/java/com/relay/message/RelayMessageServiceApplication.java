package com.relay.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RelayMessageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RelayMessageServiceApplication.class, args);
    }
}