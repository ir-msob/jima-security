/**
 * JwtReactiveRoleConverter is a converter class that extracts roles from a JSON Web Token (JWT)
 * and converts them into a Flux of GrantedAuthority objects, which are used by Spring Security.
 *
 * @author Yauqb Abdi
 * @version 1.0
 * @since 2023-11-10
 */
package ir.msob.jima.security.commons;

import ir.msob.jima.core.commons.security.ClaimKey;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts JWT to a Flux of GrantedAuthority based on the roles present in the JWT claims.
 */
public class JwtReactiveRoleConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

    /**
     * Converts a JWT into a Flux of GrantedAuthority.
     *
     * @param jwt The JSON Web Token to be converted.
     * @return A Flux of GrantedAuthority representing the roles extracted from the JWT.
     */
    @Override
    public Flux<GrantedAuthority> convert(Jwt jwt) {
        List<String> roles = extractRoles(jwt);
        return roles.isEmpty() ? Flux.empty() :
                Flux.fromIterable(roles)
                        .map(SimpleGrantedAuthority::new);
    }

    /**
     * Extracts roles from the claims of a JWT.
     *
     * @param jwt The JSON Web Token from which roles are to be extracted.
     * @return A List of strings representing the roles extracted from the JWT claims.
     */
    private List<String> extractRoles(Jwt jwt) {
        Object objectRoles = jwt.getClaims().getOrDefault(ClaimKey.ROLES, new ArrayList<>());
        return (objectRoles instanceof List) ?
                ((List<?>) objectRoles).stream()
                        .map(Object::toString)
                        .toList():
                List.of();
    }
}
