package ir.msob.jima.security.ral.keycloak.it.security;

import ir.msob.jima.core.commons.security.BaseUser;
import lombok.Builder;

import java.util.SortedSet;

/**
 * @author Yaqub Abdi
 */
public class ProjectUser extends BaseUser {
    @Builder
    public ProjectUser(String id, String sessionId, String name, String username, SortedSet<String> roles, String audience) {
        super(id, sessionId, name, username, roles, audience);
    }

    public ProjectUser() {
        super();
    }
}
