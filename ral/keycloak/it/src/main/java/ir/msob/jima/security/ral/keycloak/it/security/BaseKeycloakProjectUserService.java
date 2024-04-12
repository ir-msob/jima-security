package ir.msob.jima.security.ral.keycloak.it.security;

import com.google.common.collect.Sets;
import ir.msob.jima.core.commons.security.BaseUser;
import ir.msob.jima.core.commons.security.BaseUserService;
import ir.msob.jima.core.commons.security.ClaimKey;
import ir.msob.jima.core.commons.security.ClaimKeyValue;

import java.io.Serializable;
import java.util.*;


/**
 * @author Yaqub Abdi
 */

public interface BaseKeycloakProjectUserService extends BaseUserService {
    ProjectUser SYSTEM_USER = ProjectUser.builder()
            .id("00000000-0000-0000-0000-000000000000")
            .sessionId("00000000-0000-0000-0000-000000000000")
            .username("system")
            .roles(Sets.newTreeSet(Collections.singleton(Roles.ADMIN)))
            .audience(ClaimKeyValue.AUDIENCE_WEB)
            .build();


    @Override
    default <ID extends Comparable<ID> & Serializable, USER extends BaseUser<ID>> Optional<USER> getUser(Map<String, Object> claims) {
        SortedSet<String> roles = new TreeSet<> ( (List<String>) ((Map<String,Map<String,List<String>>>) claims.get(ProjectClaimKey.REALM_ACCESS)).get(ProjectClaimKey.KEYCLOAK_ROLES));
        return Optional.of((USER) ProjectUser.builder()
                .id(String.valueOf(claims.get(ClaimKey.ID)))
                .sessionId(String.valueOf(claims.get(ClaimKey.SESSION_ID)))
                .username(String.valueOf(claims.get(ClaimKey.SUBJECT)))
                .audience(String.valueOf(claims.get(ClaimKey.AUDIENCE)))
                .roles(roles)
                .build());
    }


    @Override
    default <ID extends Comparable<ID> & Serializable, USER extends BaseUser<ID>> Optional<USER> getSystemUser() {
        return (Optional<USER>) Optional.of(SYSTEM_USER);
    }
}
