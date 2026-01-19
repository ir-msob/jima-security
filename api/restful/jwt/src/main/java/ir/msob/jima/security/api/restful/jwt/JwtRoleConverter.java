package ir.msob.jima.security.api.restful.jwt;

import ir.msob.jima.core.commons.security.BaseClaimKey;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A converter that extracts role information from JWT (JSON Web Token) claims
 * and converts them into a collection of Spring Security {@link GrantedAuthority} objects.
 * <p>
 * This converter is intended to be used when decoding JWTs for authentication and authorization
 * purposes in Spring Security. Specifically, it looks for the claim key defined by
 * {@link BaseClaimKey#ROLES} and converts its values into {@link SimpleGrantedAuthority} instances.
 * <p>
 * Example usage:
 * <pre>{@code
 * JwtRoleConverter converter = new JwtRoleConverter();
 * Map<String, Object> claims = jwt.getClaims();
 * Collection<GrantedAuthority> authorities = converter.convert(claims);
 * }</pre>
 * <p>
 * If the 'roles' claim is missing or empty, an empty collection is returned.
 *
 * @author Yaqub Abdi
 * @since 0.1.0
 */
@Component
public class JwtRoleConverter implements Converter<@NonNull Map<String, Object>, Collection<GrantedAuthority>> {

    /**
     * Converts the 'roles' claim from JWT claims into a collection of {@link GrantedAuthority}.
     *
     * @param claims a map representing the JWT claims
     * @return a collection of GrantedAuthority objects corresponding to the roles in the JWT;
     * returns an empty collection if no roles are present
     */
    @Override
    public Collection<GrantedAuthority> convert(Map<String, Object> claims) {
        Object objectRoles = claims.getOrDefault(BaseClaimKey.ROLES, Collections.emptyList());

        if (objectRoles instanceof List roles && !roles.isEmpty()) {
            return roles.stream()
                    .map(o -> new SimpleGrantedAuthority(String.valueOf(o)))
                    .toList();
        }

        return Collections.emptyList();
    }
}
