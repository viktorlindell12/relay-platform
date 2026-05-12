package com.relay.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RelayBotServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RelayBotServiceApplication.class, args);
    }
}