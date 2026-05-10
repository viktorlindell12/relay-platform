package com.relay.user.config;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * gRPC server interceptor that enforces the same {@code x-internal-key} pre-shared secret
 * used by the HTTP {@link InternalApiKeyFilter}.
 * Spring gRPC auto-registers all {@link ServerInterceptor} beans as global interceptors,
 * so this applies to every gRPC method without any per-service wiring.
 */
@Component
public class GrpcApiKeyInterceptor implements ServerInterceptor {

    static final Metadata.Key<String> API_KEY_HEADER =
            Metadata.Key.of("x-internal-key", Metadata.ASCII_STRING_MARSHALLER);

    private final String expectedKey;

    /**
     * @param expectedKey the pre-shared key configured via {@code internal.api-key}
     * @throws IllegalArgumentException if {@code expectedKey} is blank — prevents startup with a missing secret
     */
    public GrpcApiKeyInterceptor(@Value("${internal.api-key}") String expectedKey) {
        Assert.hasText(expectedKey, "GrpcApiKeyInterceptor: expectedKey must not be blank");
        this.expectedKey = expectedKey;
    }

    /**
     * Validates the {@code x-internal-key} metadata header before allowing the call to proceed.
     * Uses constant-time comparison to prevent timing attacks.
     */
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        String provided = headers.get(API_KEY_HEADER);

        // Constant-time comparison prevents timing attacks that could leak key characters
        boolean valid = provided != null && MessageDigest.isEqual(
                expectedKey.getBytes(StandardCharsets.UTF_8),
                provided.getBytes(StandardCharsets.UTF_8)
        );

        if (!valid) {
            call.close(Status.UNAUTHENTICATED.withDescription("Missing or invalid API key"), new Metadata());
            return new ServerCall.Listener<>() {};
        }

        return next.startCall(call, headers);
    }
}