package ir.msob.jima.security.api.grpc.oauth2;

import ir.msob.jima.security.commons.JwtRoleConverter;
import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * {@code GrpcAuthenticationConfiguration} is a Spring configuration class responsible for creating and configuring
 * the gRPC authentication reader bean used in the gRPC server for OAuth2 JWT-based authentication.
 *
 * <p>The class defines a bean for the {@code GrpcAuthenticationReader} interface, utilizing the
 * {@code BearerAuthenticationReader} for handling authentication based on JWT tokens. The authentication reader is
 * configured with a {@code JwtDecoder} and a custom {@code JwtRoleConverter} to convert JWT claims into roles.
 *
 * <p>This configuration is essential for setting up the authentication mechanism for gRPC services using OAuth2 JWT tokens.
 *
 * @author Yaqub Abdi
 */
@Configuration
public class GrpcAuthenticationConfiguration {

    /**
     * Creates and configures the gRPC authentication reader bean for handling OAuth2 JWT-based authentication.
     *
     * @param jwtDecoder       The {@code JwtDecoder} used to decode JWT tokens.
     * @param jwtRoleConverter The custom {@code JwtRoleConverter} used to convert JWT claims into roles.
     * @return The configured {@code GrpcAuthenticationReader} bean.
     */
    @Bean
    GrpcAuthenticationReader authenticationReader(JwtDecoder jwtDecoder, JwtRoleConverter jwtRoleConverter) {
        return new BearerAuthenticationReader(token -> {
            Jwt jwt = jwtDecoder.decode(token);
            return new JwtAuthenticationToken(jwt, jwtRoleConverter.convert(jwt));
        });
    }
}
