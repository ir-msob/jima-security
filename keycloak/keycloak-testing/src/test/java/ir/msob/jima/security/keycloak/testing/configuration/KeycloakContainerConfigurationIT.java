package ir.msob.jima.security.keycloak.testing.configuration;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import ir.msob.jima.platform.api.properties.TestContainerProperties;
import ir.msob.jima.security.keycloak.testing.TestMicroserviceApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {TestMicroserviceApplication.class, KeycloakContainerConfiguration.class})
@ContextConfiguration
@Testcontainers
class KeycloakContainerConfigurationIT {

    @Autowired
    KeycloakContainer container;
    @Autowired
    TestContainerProperties testContainerProperties;
    @Value("${spring.security.oauth2.resource-server.jwt.issuer-uri}")
    private String configUrl;

    @Test
    @DisplayName("Container is running after initialization")
    void containerIsRunningAfterInitialization() {
        assertTrue(container.isRunning(), "Container should be running after initialization");
    }


    @Test
    @DisplayName("Properties are set correctly")
    void testContainerProperties() {
        String containerUrl = container.getAuthServerUrl() + "/realms/" + testContainerProperties.getKeycloak().getRealm();
        assertEquals(containerUrl, configUrl);
    }

}
