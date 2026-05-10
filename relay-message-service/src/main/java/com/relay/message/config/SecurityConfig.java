package com.relay.message.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Instant;

/**
 * Locks down all endpoints to internal callers only.
 * Requests must supply the correct {@code X-Internal-Key} header — no public routes exist
 * beyond the actuator health check used by Docker.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${internal.api-key}")
    private String internalApiKey;

    /**
     * Defines the security filter chain with stateless session management and API-key enforcement.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF disabled: this service is stateless and uses bearer token auth via X-Internal-Key header.
                // No session cookies or browser-based authentication are used — classical CSRF attack is not applicable.
                .csrf(AbstractHttpConfigurer::disable) // codeql[java/spring-disabled-csrf-protection]
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().hasRole("INTERNAL")
                )
                .addFilterBefore(new InternalApiKeyFilter(internalApiKey), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                writeError(res, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((req, res, e) ->
                                writeError(res, HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                )
                .build();
    }

    private void writeError(HttpServletResponse response, int status, String message) throws java.io.IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"status\":%d,\"message\":\"%s\",\"timestamp\":\"%s\"}".formatted(status, message, Instant.now())
        );
    }
}