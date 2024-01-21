package ir.msob.jima.security.api.grpc.oauth2;

import io.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.common.security.SecurityConstants;
import net.devh.boot.grpc.common.util.InterceptorOrder;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.interceptors.AuthenticatingServerInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * {@code BaseAuthenticatingServerInterceptor} is a gRPC server interceptor that handles authentication based on JWT tokens.
 * It implements the {@code AuthenticatingServerInterceptor} interface and is marked as a global server interceptor.
 * This interceptor extracts the JWT token from the authorization header in the metadata, validates it using the provided
 * {@code GrpcAuthenticationReader}, and sets the authenticated user in the security context.
 *
 * @author Yaqub Abdi
 */
@GrpcGlobalServerInterceptor
@Order(InterceptorOrder.ORDER_SECURITY_AUTHENTICATION)
@Slf4j
@RequiredArgsConstructor
public class BaseAuthenticatingServerInterceptor implements AuthenticatingServerInterceptor {

    /**
     * The {@code GrpcAuthenticationReader} used to read and validate JWT tokens.
     */
    private final GrpcAuthenticationReader jwtGrpcAuthenticationReader;

    /**
     * Intercepts the gRPC server call, extracts the JWT token from the authorization header,
     * validates it, and sets the authenticated user in the security context.
     *
     * @param serverCall        The gRPC server call being intercepted.
     * @param metadata          Metadata associated with the call.
     * @param serverCallHandler The server call handler.
     * @param <ReqT>            The type of the request.
     * @param <RespT>           The type of the response.
     * @return A {@code ServerCall.Listener} for processing the server call.
     * @throws StatusRuntimeException If authentication fails or an error occurs during the process.
     */
    @SneakyThrows
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {

        String header = metadata.get(SecurityConstants.AUTHORIZATION_HEADER);

        if (StringUtils.isEmpty(header)) {
            log.debug("Authentication header is null");
            return serverCallHandler.startCall(serverCall, metadata);
        }

        try {
            Authentication authentication = jwtGrpcAuthenticationReader.readAuthentication(serverCall, metadata);

            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                /* For reactive
                ReactiveSecurityContextHolder
                        .getContext()
                        .map(securityContext -> {
                            if (securityContext.getAuthentication() == null)
                                securityContext.setAuthentication(authentication);
                            return securityContext;
                        })
                        .toFuture()
                        .get();
                 */
                return serverCallHandler.startCall(serverCall, metadata);
            } else {
                throw Status.UNAUTHENTICATED.withDescription("Authentication is invalid!").asRuntimeException();
            }
        } catch (Exception e) {
            ReactiveSecurityContextHolder.clearContext();
            log.error("Authentication request failed: {}", e.getMessage(), e);
            throw Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e).asRuntimeException();
        }
    }
}




