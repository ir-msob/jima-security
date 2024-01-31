package ir.msob.jima.security.api.rsocket.it;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

@Configuration
@EnableReactiveMethodSecurity
@EnableRSocketSecurity
public class RSocketSecurityConfiguration {

    @Bean
    public PayloadSocketAcceptorInterceptor authorizationToken(RSocketSecurity rSocketSecurity, JwtReactiveAuthenticationManager authenticationManager) {
        rSocketSecurity
                .authorizePayload(authorize ->
                        authorize
                                .anyRequest().authenticated()
                                .anyExchange().permitAll()
                )
                .jwt(jwtSpec ->
                        jwtSpec.authenticationManager(authenticationManager)
                );

        return rSocketSecurity.build();
    }

}