package ir.msob.jima.security.core.jwt;

import ir.msob.jima.security.api.token.RoleMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DefaultRoleMapper implements RoleMapper {

    @Override
    public List<String> extractRoles(Map<String, Object> claims) {
        Object roles = claims.get("roles");

        if (roles instanceof List<?> list) {
            return list.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .toList();
        }

        return List.of();
    }
}