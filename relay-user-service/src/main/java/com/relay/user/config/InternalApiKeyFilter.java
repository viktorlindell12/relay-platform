package com.relay.user.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

/**
 * Validates the {@code X-Internal-Key} header on every request.
 * Only callers that present the correct pre-shared key are granted the {@code ROLE_INTERNAL} authority.
 * Requests without the header pass through unauthenticated — route-level rules in
 * {@link SecurityConfig} reject them.
 */
public class InternalApiKeyFilter extends OncePerRequestFilter {

    static final String HEADER = "X-Internal-Key";

    private final String expectedKey;

    /**
     * @param expectedKey the pre-shared key configured via {@code internal.api-key}
     * @throws IllegalArgumentException if {@code expectedKey} is blank — prevents startup with a missing secret
     */
    public InternalApiKeyFilter(String expectedKey) {
        Assert.hasText(expectedKey, "InternalApiKeyFilter: expectedKey must not be blank");
        this.expectedKey = expectedKey;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String key = request.getHeader(HEADER);

        // Constant-time comparison prevents timing attacks that could leak key characters
        if (key != null && MessageDigest.isEqual(
                expectedKey.getBytes(StandardCharsets.UTF_8),
                key.getBytes(StandardCharsets.UTF_8))) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    "internal-caller", null, List.of(new SimpleGrantedAuthority("ROLE_INTERNAL"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}