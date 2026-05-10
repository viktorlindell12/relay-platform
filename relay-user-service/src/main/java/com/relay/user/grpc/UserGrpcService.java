package com.relay.user.grpc;

import com.relay.proto.user.GetUserByIdRequest;
import com.relay.proto.user.UserResponse;
import com.relay.proto.user.UserServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

/**
 * gRPC implementation of {@code UserService} defined in {@code user.proto}.
 * Full implementation is added in the user-profile feature issue.
 */
@Service
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    /**
     * Returns the profile for the requested user ID.
     * Currently returns UNIMPLEMENTED until the service layer is wired up.
     *
     * @param request          contains the auth-service user ID to look up
     * @param responseObserver receives the {@link UserResponse} or an error
     */
    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<UserResponse> responseObserver) {
        responseObserver.onError(
                Status.UNIMPLEMENTED.withDescription("Not yet implemented").asRuntimeException()
        );
    }
}