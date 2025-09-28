package ir.msob.jima.security.api.grpc.oauth2;

import org.springframework.security.core.Authentication;

/**
 * Functional interface for reading and validating authentication from a token.
 * Implementations of this interface are responsible for parsing a token (typically JWT)
 * and returning a valid Spring Security {@link Authentication} object.
 * <p>
 * If the token is invalid, expired, or cannot be authenticated, implementations should return {@code null}
 * or throw an appropriate exception.
 *
 * @author Yaqub Abdi
 */
@FunctionalInterface
public interface AuthenticationReader {

    /**
     * Reads and validates authentication information from the provided token.
     *
     * @param token the authentication token (typically a JWT token)
     * @return a fully authenticated {@link Authentication} object if the token is valid,
     * {@code null} if the token is invalid or cannot be authenticated
     */
    Authentication readAuthentication(String token);
}