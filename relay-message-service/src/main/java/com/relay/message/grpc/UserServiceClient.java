package com.relay.message.grpc;

import com.relay.proto.user.GetUserByIdRequest;
import com.relay.proto.user.UserServiceGrpc;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

/**
 * Wraps the gRPC stub for relay-user-service.
 * Resolves a sender's display name for message response enrichment.
 */
@Component
public class UserServiceClient {

    private static final Logger log = LoggerFactory.getLogger(UserServiceClient.class);
    private static final String FALLBACK_DISPLAY_NAME = "Unknown User";

    private final UserServiceGrpc.UserServiceBlockingStub stub;

    public UserServiceClient(GrpcChannelFactory channelFactory) {
        this.stub = UserServiceGrpc.newBlockingStub(channelFactory.createChannel("user-service"));
    }

    /**
     * Resolves the display name for the given auth-service user ID.
     * Returns {@value #FALLBACK_DISPLAY_NAME} if User Service is unreachable or the user is not found,
     * so a gRPC outage never breaks message reads.
     *
     * @param authUserId the auth-service user ID of the message sender
     * @return display name, or {@value #FALLBACK_DISPLAY_NAME} on any failure
     */
    public String getDisplayName(Long authUserId) {
        try {
            return stub.getUserById(
                    GetUserByIdRequest.newBuilder().setUserId(authUserId).build()
            ).getDisplayName();
        } catch (StatusRuntimeException e) {
            log.warn("User service call failed for userId={}: {}", authUserId, e.getStatus());
            return FALLBACK_DISPLAY_NAME;
        } catch (Exception e) {
            log.warn("Unexpected error resolving display name for userId={}", authUserId, e);
            return FALLBACK_DISPLAY_NAME;
        }
    }
}