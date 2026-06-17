package ir.msob.jima.security.reactive.jwt;

import ir.msob.jima.security.api.token.RoleMapper;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;

import java.util.List;


public class JwtReactiveRoleConverter implements Converter<@NonNull Jwt, Flux<GrantedAuthority>> {

    private final RoleMapper roleMapper;

    public JwtReactiveRoleConverter(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public Flux<GrantedAuthority> convert(Jwt jwt) {

        List<String> roles = roleMapper.extractRoles(jwt.getClaims());

        return Flux.fromIterable(roles)
                .map(SimpleGrantedAuthority::new)
                .map(g -> (GrantedAuthority) g);
    }
}
