package ir.msob.jima.security.keycloak.testing.configuration;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import ir.msob.jima.platform.api.properties.TestContainerProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.util.StringUtils;
import org.testcontainers.images.PullPolicy;

/**
 * This class provides the configuration for setting up a Keycloak container for testing purposes.
 * It is annotated with @TestConfiguration to indicate that it is a source of bean definitions.
 * The proxyBeanMethods attribute is set to false to optimize runtime bean creation.
 */
@TestConfiguration(proxyBeanMethods = false)
public class KeycloakContainerConfiguration {

    public static void registry(DynamicPropertyRegistry registry, KeycloakContainer container, TestContainerProperties testContainerProperties) {
        registry.add("spring.security.oauth2.resource-server.jwt.issuer-uri",
                () -> container.getAuthServerUrl() + "/realms/" + testContainerProperties.getKeycloak().getRealm());
    }

    /**
     * This method creates a KeycloakContainer bean for testing purposes.
     * It uses the DynamicPropertyRegistry to dynamically register properties for the Keycloak container.
     * The property includes the URI for the Keycloak container.
     * The JimaProperties object is used to get the Docker image name for the Keycloak container.
     *
     * @param TestContainerProperties The TestContainerProperties object used to get the Docker image name for the Keycloak container.
     * @return The created KeycloakContainer bean.
     */
    @Bean
    public KeycloakContainer keycloakContainer(TestContainerProperties testContainerProperties) {
        String keycloakImage = StringUtils.hasText(testContainerProperties.getKeycloak().getImage()) ?
                testContainerProperties.getKeycloak().getImage() : null;

        KeycloakContainer container = StringUtils.hasText(keycloakImage) ?
                new KeycloakContainer(keycloakImage) : new KeycloakContainer();

        container.withReuse(testContainerProperties.getKeycloak().isReuse());
        container.withImagePullPolicy(PullPolicy.defaultPolicy());

        container.withRealmImportFile(testContainerProperties.getKeycloak().getRealmJsonFile());
        return container;
    }
}
