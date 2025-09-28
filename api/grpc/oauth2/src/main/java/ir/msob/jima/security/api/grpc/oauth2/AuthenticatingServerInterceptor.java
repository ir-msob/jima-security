package ir.msob.jima.security.api.grpc.oauth2;

import io.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * gRPC server interceptor that handles authentication based on JWT tokens.
 * This interceptor extracts the JWT token from the authorization header in the metadata,
 * validates it using the provided {@code AuthenticationReader}, and sets the authenticated
 * user in the Spring Security context.
 * <p>
 * If no authorization header is present, the call continues without authentication.
 * If the token is invalid or authentication fails, the call is rejected with UNAUTHENTICATED status.
 *
 * @author Yaqub Abdi
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class AuthenticatingServerInterceptor implements ServerInterceptor {

    /**
     * Constant for Bearer token prefix length.
     */
    private static final int BEARER_PREFIX_LENGTH = 7;

    /**
     * Metadata key for authorization header.
     */
    private static final Metadata.Key<String> AUTHORIZATION_KEY =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    /**
     * Service for reading and validating JWT tokens.
     */
    private final AuthenticationReader authenticationReader;

    /**
     * Intercepts the gRPC server call to perform JWT token authentication.
     *
     * @param serverCall        the gRPC server call being intercepted
     * @param metadata          metadata associated with the call containing the authorization header
     * @param serverCallHandler the server call handler to delegate to after authentication
     * @param <ReqT>            the type of the request
     * @param <RespT>           the type of the response
     * @return a {@code ServerCall.Listener} for processing the server call
     * @throws StatusRuntimeException if authentication fails with UNAUTHENTICATED status
     */
    @SneakyThrows
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> serverCall,
            Metadata metadata,
            ServerCallHandler<ReqT, RespT> serverCallHandler) {

        log.debug("Intercepting gRPC call for authentication");

        String authorizationHeader = metadata.get(AUTHORIZATION_KEY);

        if (StringUtils.isBlank(authorizationHeader)) {
            log.debug("Authorization header is missing, proceeding without authentication");
            return serverCallHandler.startCall(serverCall, metadata);
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            log.warn("Invalid authorization header format. Expected Bearer token");
            throw Status.UNAUTHENTICATED
                    .withDescription("Invalid authorization format. Expected: Bearer <token>")
                    .asRuntimeException();
        }

        try {
            String token = authorizationHeader.substring(BEARER_PREFIX_LENGTH).trim();

            if (StringUtils.isBlank(token)) {
                log.warn("Bearer token is empty");
                throw Status.UNAUTHENTICATED
                        .withDescription("Bearer token is empty")
                        .asRuntimeException();
            }

            log.debug("Attempting to authenticate with JWT token");
            Authentication authentication = authenticationReader.readAuthentication(token);

            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("Authentication failed: invalid or expired token");
                throw Status.UNAUTHENTICATED
                        .withDescription("Authentication failed: invalid or expired token")
                        .asRuntimeException();
            }

            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Authentication successful for principal: {}", authentication.getName());

            return serverCallHandler.startCall(serverCall, metadata);

        } catch (StatusRuntimeException e) {
            // Re-throw gRPC status exceptions
            SecurityContextHolder.clearContext();
            log.error("Authentication failed with gRPC status: {}", e.getStatus().getDescription());
            throw e;
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.error("Authentication request failed: {}", e.getMessage(), e);
            throw Status.UNAUTHENTICATED
                    .withDescription("Authentication processing failed")
                    .withCause(e)
                    .asRuntimeException();
        }
    }
}