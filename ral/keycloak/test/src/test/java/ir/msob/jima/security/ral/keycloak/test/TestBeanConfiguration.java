package ir.msob.jima.security.ral.keycloak.test;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import ir.msob.jima.core.beans.properties.JimaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;

@TestConfiguration
public class TestBeanConfiguration {

    @Bean
    public DynamicPropertyRegistrar dynamicPropertyRegistrar(KeycloakContainer kafkaContainer, JimaProperties jimaProperties) {
        return registry -> {
            KeycloakContainerConfiguration.registry(registry, kafkaContainer, jimaProperties);
        };
    }
}
