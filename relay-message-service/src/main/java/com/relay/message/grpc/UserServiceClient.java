package com.relay.message.grpc;

import org.springframework.stereotype.Component;

/**
 * Wraps the gRPC stub for relay-user-service.
 * Used to resolve a sender's display name when enriching message responses.
 * Stub injection and channel configuration will be wired in the message-send feature issue.
 */
@Component
public class UserServiceClient {

    // gRPC stub will be injected here in the feature issue that requires user enrichment.
    // The spring-grpc-client-spring-boot-starter channel is configured in application.yml
    // under spring.grpc.client.channels.user-service.

}