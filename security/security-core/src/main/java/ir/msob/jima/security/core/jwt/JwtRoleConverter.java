package ir.msob.jima.security.core.jwt;

import ir.msob.jima.security.api.token.RoleMapper;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;


public class JwtRoleConverter implements Converter<@NonNull Jwt, Collection<GrantedAuthority>> {

    private final RoleMapper roleMapper;

    public JwtRoleConverter(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        List<String> roles = roleMapper.extractRoles(jwt.getClaims());

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .map(g -> (GrantedAuthority) g)
                .toList();
    }

}
