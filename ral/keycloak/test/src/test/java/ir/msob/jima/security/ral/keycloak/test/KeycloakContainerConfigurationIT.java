package ir.msob.jima.security.ral.keycloak.test;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import ir.msob.jima.core.beans.properties.JimaProperties;
import lombok.extern.apachecommons.CommonsLog;
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
@CommonsLog
class KeycloakContainerConfigurationIT {

    @Autowired
    KeycloakContainer container;
    @Autowired
    JimaProperties jimaProperties;
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
        String containerUrl = container.getAuthServerUrl() + "/realms/" + jimaProperties.getTestContainer().getKeycloak().getRealm();
        assertEquals(containerUrl, configUrl);
    }

}
