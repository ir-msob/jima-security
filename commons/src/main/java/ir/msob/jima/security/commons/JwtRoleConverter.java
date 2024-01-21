package ir.msob.jima.security.commons;

import ir.msob.jima.core.commons.security.ClaimKey;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Converter class for extracting and converting JWT (JSON Web Token) claims related
 * to roles into a collection of Spring Security GrantedAuthority objects.
 * <p>
 * This class implements the Spring Framework Converter interface and is intended to
 * be used as part of the JWT decoding process to extract and convert role information
 * from the JWT claims.
 * <p>
 * Usage:
 * This converter is typically used in conjunction with a ReactiveJwtDecoder when
 * configuring JWT decoding in a Spring Security context. It converts the 'roles' claim
 * from the JWT into a collection of GrantedAuthority objects.
 * <p>
 * Example:
 * ```java
 * Jwt = // ... obtain JWT from the authentication token
 * Collection<GrantedAuthority> authorities = jwtRoleConverter.convert(jwt);
 * // ... make use of the authorities in your security logic
 * ```
 *
 * @author Yaqub Abdi
 * @since 0.1.0
 */
public class JwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    /**
     * Converts the 'roles' claim from the JWT into a collection of GrantedAuthority objects.
     *
     * @param jwt The JWT from which to extract roles.
     * @return A collection of GrantedAuthority objects representing the roles.
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Extract 'roles' claim from JWT claims
        Object objectRoles = jwt.getClaims().getOrDefault(ClaimKey.ROLES, Collections.emptyList());

        // Check if 'roles' claim is a non-empty list
        if (objectRoles instanceof List roles && !roles.isEmpty()) {
            // Convert roles to SimpleGrantedAuthority objects
            return roles.stream()
                    .map(o -> new SimpleGrantedAuthority(String.valueOf(o)))
                    .toList();
        }

        // Return an empty collection if 'roles' claim is not present or empty
        return Collections.emptyList();
    }
}
