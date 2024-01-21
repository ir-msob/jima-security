package ir.msob.jima.security.commons;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for JWT-related beans.
 */
@Configuration
public class JwtRoleConvertorConfiguration {

    /**
     * Bean definition for JwtRoleConverter.
     *
     * @return An instance of JwtRoleConverter.
     */
    @Bean
    JwtRoleConverter jwtRoleConverter() {
        return new JwtRoleConverter();
    }

    /**
     * Creates and configures a JwtReactiveRoleConverter bean.
     *
     * @return JwtReactiveRoleConverter bean used for converting JWTs to a Flux of GrantedAuthority.
     */
    @Bean
    JwtReactiveRoleConverter jwtReactiveRoleConverter() {
        return new JwtReactiveRoleConverter();
    }
}
