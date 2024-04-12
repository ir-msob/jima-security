package ir.msob.jima.security.ral.keycloak.test;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import ir.msob.jima.core.beans.properties.JimaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.util.StringUtils;

/**
 * This class provides the configuration for setting up a Keycloak container for testing purposes.
 * It is annotated with @TestConfiguration to indicate that it is a source of bean definitions.
 * The proxyBeanMethods attribute is set to false to optimize runtime bean creation.
 */
@TestConfiguration(proxyBeanMethods = false)
public class KeycloakContainerConfiguration {

    /**
     * This method creates a KeycloakContainer bean for testing purposes.
     * It uses the DynamicPropertyRegistry to dynamically register properties for the Keycloak container.
     * The property includes the URI for the Keycloak container.
     * The JimaProperties object is used to get the Docker image name for the Keycloak container.
     *
     * @param registry       The DynamicPropertyRegistry used to dynamically register properties for the Keycloak container.
     * @param jimaProperties The JimaProperties object used to get the Docker image name for the Keycloak container.
     * @return The created KeycloakContainer bean.
     */
    @Bean
    public KeycloakContainer keycloakContainer(DynamicPropertyRegistry registry, JimaProperties jimaProperties) {
        String keycloakImage = StringUtils.hasText(jimaProperties.getTestContainer().getKeycloak().getImage()) ?
                jimaProperties.getTestContainer().getKeycloak().getImage() : null;

        KeycloakContainer container = StringUtils.hasText(keycloakImage) ?
                new KeycloakContainer(keycloakImage) : new KeycloakContainer();

        container.withReuse(jimaProperties.getTestContainer().getKeycloak().isReuse());

        container.withRealmImportFile(jimaProperties.getTestContainer().getKeycloak().getRealmJsonFile());

        registry.add("spring.security.oauth2.resource-server.jwt.issuer-uri",
                () -> container.getAuthServerUrl() + "/realms/" + jimaProperties.getTestContainer().getKeycloak().getRealm());

        return container;
    }
}
