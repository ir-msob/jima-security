package ir.msob.jima.security.api.token;

import java.util.List;
import java.util.Map;

public interface RoleMapper {
    List<String> extractRoles(Map<String, Object> claims);
}