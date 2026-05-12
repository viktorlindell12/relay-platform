package com.relay.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RelayBffServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RelayBffServiceApplication.class, args);
    }
}