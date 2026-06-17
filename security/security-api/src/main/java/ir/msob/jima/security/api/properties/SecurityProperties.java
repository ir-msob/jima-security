package ir.msob.jima.security.api.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * The `SecurityProperties` class is a simple POJO (Plain Old Java Object) that holds security-embeddeddomain properties.
 * It uses Lombok annotations for automatic generation of getters, setters, a no-argument constructor, and a toString method.
 * The `defaultClientRegistrationId` field is used to store the default client registration ID for the security configuration.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class SecurityProperties {

    public static final String DEFAULT_CLIENT_ID = "service-client";
    /**
     * The default client registration ID for the security configuration.
     * Default value is "service-client".
     */
    private String defaultClientRegistrationId = DEFAULT_CLIENT_ID;

    private JwtProperties accessToken = new JwtProperties();
    private JwtProperties refreshToken = new JwtProperties();
    private JwtProperties clientCredentialsToken = new JwtProperties();


    @Setter
    @Getter
    @NoArgsConstructor
    @ToString
    public static class JwtProperties {
        private String secret = "secret";
        private Long expiration = 360L;
        private String algorithm = "HS256";
        private Map<String, Object> claims = Map.of("sub", DEFAULT_CLIENT_ID);
    }
}
