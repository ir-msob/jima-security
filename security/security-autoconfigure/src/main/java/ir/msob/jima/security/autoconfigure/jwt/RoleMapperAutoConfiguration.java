package ir.msob.jima.security.autoconfigure.jwt;

import ir.msob.jima.security.api.token.RoleMapper;
import ir.msob.jima.security.core.jwt.DefaultRoleMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
public class RoleMapperAutoConfiguration {

    @Bean
    RoleMapper roleMapper() {
        return new DefaultRoleMapper();
    }

}
