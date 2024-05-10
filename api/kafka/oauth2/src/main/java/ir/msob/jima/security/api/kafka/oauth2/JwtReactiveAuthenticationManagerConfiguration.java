package ir.msob.jima.security.api.kafka.oauth2;

import ir.msob.jima.security.commons.JwtRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;


@Configuration
public class JwtReactiveAuthenticationManagerConfiguration {

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

}

