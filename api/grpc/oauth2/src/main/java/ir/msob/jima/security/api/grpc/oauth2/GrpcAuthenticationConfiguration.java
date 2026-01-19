package ir.msob.jima.security.api.grpc.oauth2;

import ir.msob.jima.core.commons.logger.Logger;
import ir.msob.jima.core.commons.logger.LoggerFactory;
import ir.msob.jima.security.commons.JwtRoleConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Spring configuration class for setting up gRPC JWT-based authentication.
 * <p>
 * This class provides the necessary beans to authenticate gRPC requests using OAuth2 JWT tokens.
 * It configures an {@link AuthenticationReader} that decodes JWT tokens and converts them
 * into Spring Security authentication objects with appropriate roles.
 * </p>
 *
 * @author Yaqub Abdi
 */
@Configuration
@RequiredArgsConstructor
public class GrpcAuthenticationConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(GrpcAuthenticationConfiguration.class);

    /**
     * Creates an {@link AuthenticationReader} bean for JWT token authentication.
     * <p>
     * The reader decodes JWT tokens using the provided {@link JwtDecoder}, extracts roles
     * using the {@link JwtRoleConverter}, and creates a fully authenticated {@link JwtAuthenticationToken}.
     * </p>
     *
     * @param jwtDecoder       the JWT decoder used to validate and decode tokens
     * @param jwtRoleConverter the converter used to extract roles from JWT claims
     * @return a configured {@link AuthenticationReader} instance
     * @throws JwtException             if the token is invalid, expired, or malformed
     * @throws IllegalArgumentException if the token is null or empty
     */
    @Bean
    public AuthenticationReader authenticationReader(JwtDecoder jwtDecoder, JwtRoleConverter jwtRoleConverter) {
        logger.debug("Creating AuthenticationReader bean with JWT decoder and role converter");

        return token -> {
            if (token == null || token.trim().isEmpty()) {
                logger.warn("Attempted to authenticate with null or empty token");
                throw new IllegalArgumentException("Token cannot be null or empty");
            }

            try {
                logger.debug("Decoding JWT token for authentication");
                Jwt jwt = jwtDecoder.decode(token);

                logger.debug("Converting JWT claims to authorities");
                var authorities = jwtRoleConverter.convert(jwt);

                logger.debug("Creating JwtAuthenticationToken with {} authorities", authorities);
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);

                logger.debug("Authentication created for subject: {}", jwt.getSubject());

                return authentication;

            } catch (JwtException e) {
                logger.warn("JWT token validation failed: {}", e.getMessage());
                throw e;
            } catch (Exception e) {
                logger.error("Unexpected error during authentication: {}", e.getMessage(), e);
                throw new RuntimeException("Authentication processing failed", e);
            }
        };
    }
}