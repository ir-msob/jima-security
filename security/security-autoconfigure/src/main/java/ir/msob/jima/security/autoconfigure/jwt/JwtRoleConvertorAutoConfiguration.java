package ir.msob.jima.security.autoconfigure.jwt;

import ir.msob.jima.security.api.token.RoleMapper;
import ir.msob.jima.security.core.jwt.JwtRoleConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JwtRoleConvertorAutoConfiguration {

    @Bean
    JwtRoleConverter jwtRoleConverter(RoleMapper mapper) {
        return new JwtRoleConverter(mapper);
    }

}
