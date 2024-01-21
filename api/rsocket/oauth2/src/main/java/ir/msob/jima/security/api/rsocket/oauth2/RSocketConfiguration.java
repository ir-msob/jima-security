package ir.msob.jima.security.api.rsocket.oauth2;

import ir.msob.jima.security.commons.JwtRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;

/**
 * Configuration class for setting up RSocket-specific configurations related to security
 * and message handling in a Spring application using OAuth2 for authentication.
 * <p>
 * This class provides bean definitions for configuring JWT (JSON Web Token) authentication
 * for RSocket communication, including the authentication manager and RSocket message handler.
 * <p>
 * Usage:
 * Include this configuration class in your application's configuration to enable RSocket
 * security with OAuth2-based JWT authentication.
 * <p>
 * Example:
 * ```java
 *
 * @author Yaqub Abdi
 * @Configuration
 * @Import(RSocketConfiguration.class) public class RSocketSecurityConfig {
 * // Your additional RSocket and security configurations go here
 * }
 * ```
 * @since 0.1.0
 */
@Configuration
public class RSocketConfiguration {

    /**
     * Bean definition for JwtReactiveAuthenticationManager.
     *
     * @param decoder          The ReactiveJwtDecoder for decoding JWTs.
     * @param jwtRoleConverter The JwtRoleConverter for converting JWT roles to authorities.
     * @return An instance of JwtReactiveAuthenticationManager configured with the provided decoder and converter.
     */
    @Bean
    public JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager(ReactiveJwtDecoder decoder, JwtRoleConverter jwtRoleConverter) {
        // Configure JwtAuthenticationConverter with the provided JwtRoleConverter
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtRoleConverter);

        // Configure JwtReactiveAuthenticationManager with the provided decoder and converter
        JwtReactiveAuthenticationManager manager = new JwtReactiveAuthenticationManager(decoder);
        manager.setJwtAuthenticationConverter(new ReactiveJwtAuthenticationConverterAdapter(converter));

        return manager;
    }

    /**
     * Bean definition for RSocketMessageHandler.
     *
     * @param strategies The RSocketStrategies to be used by the message handler.
     * @return An instance of RSocketMessageHandler configured with the provided strategies.
     */
    @Bean
    public RSocketMessageHandler messageHandler(RSocketStrategies strategies) {
        // Create and configure RSocketMessageHandler
        RSocketMessageHandler mh = new RSocketMessageHandler();
        mh.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        mh.setRSocketStrategies(strategies);

        return mh;
    }

}

