package ir.msob.jima.security.kafka.autoconfigure.jwt;

import ir.msob.jima.security.core.jwt.JwtRoleConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;


@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class JwtReactiveAuthenticationManagerAutoConfiguration {

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

