package ir.msob.jima.security.api.restful.jwt;

import java.util.Map;

/**
 * Base interface for JWT (JSON Web Token) services.
 * <p>
 * Implementations of this interface are responsible for generating JWT tokens
 * with custom claims, expiration, and signing algorithm, as well as validating
 * and parsing existing tokens.
 * <p>
 * This interface is intended to be used in Spring or other Java applications
 * that need to issue or verify JWTs for authentication or service-to-service
 * communication.
 * <p>
 * Example usage:
 * <pre>{@code
 * BaseJwtTokenService tokenService = new JwtTokenService();
 * Map<String, Object> claims = Map.of("sub", "user123", "roles", List.of("ROLE_USER"));
 * String token = tokenService.generateToken("secretKey", 3600000L, "HS256", claims);
 * Map<String, Object> parsedClaims = tokenService.validateAndParse(token, "secretKey", 3600000L, "HS256");
 * }</pre>
 *
 * @author
 * @since 0.1.0
 */
public interface BaseJwtTokenService {

    /**
     * Generates a JWT token with the specified secret, expiration time, signing algorithm, and claims.
     *
     * @param secret     the secret key used to sign the token
     * @param expiration the expiration time in milliseconds from now
     * @param algorithm  the signing algorithm (e.g., HS256, HS384, HS512)
     * @param claims     a map of claims to include in the token payload
     * @return the generated JWT token as a compact string
     * @throws IllegalArgumentException if the expiration is invalid or any required parameter is missing
     */
    String generateToken(String secret,
                         Long expiration,
                         String algorithm,
                         Map<String, Object> claims);

    /**
     * Validates the given JWT token and parses its claims.
     * <p>
     * This method should verify the token's signature using the provided secret
     * and algorithm. It may also check the expiration and throw an exception if
     * the token is expired or invalid.
     *
     * @param token      the JWT token to validate and parse
     * @param secret     the secret key used to validate the token's signature
     * @param expiration the expiration time in milliseconds (optional, may be used for additional checks)
     * @param algorithm  the signing algorithm used to validate the token (e.g., HS256, HS384, HS512)
     * @return a map containing the claims extracted from the token
     * @throws IllegalArgumentException if the token is invalid or expired
     */
    Map<String, Object> validateAndParse(String token,
                                         String secret,
                                         Long expiration,
                                         String algorithm);
}
