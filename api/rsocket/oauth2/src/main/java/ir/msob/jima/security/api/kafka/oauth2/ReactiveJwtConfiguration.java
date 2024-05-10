package ir.msob.jima.security.api.kafka.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;

/**
 * Configuration class for JWT (JSON Web Token) in the context of OAuth2 security
 * for Reactive Spring applications using RSocket.
 * <p>
 * This class is responsible for configuring and providing the necessary components
 * related to JWT decoding and role conversion.
 * <p>
 * Usage:
 * Include this configuration class in your application's configuration to enable
 * JWT authentication and authorization in the context of an RSocket-based API.
 * <p>
 * @since 0.1.0
 */
@Configuration
public class ReactiveJwtConfiguration {

    /**
     * Configuration property representing the issuer URI of the JWT.
     */
    @Value("${spring.security.oauth2.resource-server.jwt.issuer-uri}")
    private String issuerUri;

    /**
     * Bean definition for ReactiveJwtDecoder.
     *
     * @return An instance of ReactiveJwtDecoder configured with the issuer URI.
     */
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return ReactiveJwtDecoders
                .fromIssuerLocation(issuerUri);
    }
}
