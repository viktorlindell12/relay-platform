package com.relay.user.grpc;

import com.relay.proto.user.GetUserByIdRequest;
import com.relay.proto.user.UserResponse;
import com.relay.proto.user.UserServiceGrpc;
import com.relay.user.repository.UserProfileRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

/**
 * gRPC implementation of {@code UserService} defined in {@code user.proto}.
 * Allows internal services (e.g. Message Service) to resolve user info by auth-service user ID
 * without routing through the BFF.
 */
@Service
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserProfileRepository userProfileRepository;

    public UserGrpcService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Returns the profile for the requested auth-service user ID.
     *
     * @param request          contains the auth-service user ID to look up
     * @param responseObserver receives the {@link UserResponse} or a gRPC status error
     */
    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<UserResponse> responseObserver) {
        if (request.getUserId() <= 0) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription("user_id must be a positive number").asRuntimeException()
            );
            return;
        }

        userProfileRepository.findByAuthUserId(request.getUserId())
                .ifPresentOrElse(
                        profile -> {
                            UserResponse response = UserResponse.newBuilder()
                                    .setId(profile.getAuthUserId())
                                    .setEmail(profile.getEmail())
                                    .setDisplayName(profile.getDisplayName())
                                    .build();
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        () -> responseObserver.onError(
                                Status.NOT_FOUND
                                        .withDescription("user not found")
                                        .asRuntimeException()
                        )
                );
    }
}