package com.relay.bff.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * Configures one {@link WebClient} per downstream service.
 * Each client shares a common connector with a 5 s connect timeout and 10 s response timeout.
 * Clients for internal services ({@code user}, {@code message}) attach the pre-shared
 * {@code X-Internal-Key} header on every request.
 */
@Configuration
public class WebClientConfig {

    private static final int CONNECT_TIMEOUT_MS = 5_000;
    private static final Duration RESPONSE_TIMEOUT = Duration.ofSeconds(10);

    @Bean("authWebClient")
    public WebClient authWebClient(ServiceProperties props) {
        return WebClient.builder()
                .clientConnector(buildConnector())
                .baseUrl(props.authUrl())
                .build();
    }

    @Bean("userWebClient")
    public WebClient userWebClient(ServiceProperties props, InternalApiKeyProperties keyProps) {
        return WebClient.builder()
                .clientConnector(buildConnector())
                .baseUrl(props.userUrl())
                .defaultHeader("X-Internal-Key", keyProps.apiKey())
                .build();
    }

    @Bean("messageWebClient")
    public WebClient messageWebClient(ServiceProperties props, InternalApiKeyProperties keyProps) {
        return WebClient.builder()
                .clientConnector(buildConnector())
                .baseUrl(props.messageUrl())
                .defaultHeader("X-Internal-Key", keyProps.apiKey())
                .build();
    }

    private ReactorClientHttpConnector buildConnector() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MS)
                .responseTimeout(RESPONSE_TIMEOUT);
        return new ReactorClientHttpConnector(httpClient);
    }
}