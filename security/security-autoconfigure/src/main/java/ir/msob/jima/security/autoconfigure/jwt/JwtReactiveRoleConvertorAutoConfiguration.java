package ir.msob.jima.security.autoconfigure.jwt;

import ir.msob.jima.security.api.token.RoleMapper;
import ir.msob.jima.security.reactive.jwt.JwtReactiveRoleConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class JwtReactiveRoleConvertorAutoConfiguration {

    @Bean
    JwtReactiveRoleConverter jwtReactiveRoleConverter(RoleMapper mapper) {
        return new JwtReactiveRoleConverter(mapper);
    }

}
