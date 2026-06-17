package ir.msob.jima.security.keycloak.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import ir.msob.jima.platform.api.properties.TestContainerProperties;
import ir.msob.jima.security.keycloak.testing.configuration.KeycloakContainerConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;

@TestConfiguration
public class TestBeanConfiguration {

    @Bean
    public DynamicPropertyRegistrar dynamicPropertyRegistrar(KeycloakContainer kafkaContainer, TestContainerProperties testContainerProperties) {
        return registry ->
                KeycloakContainerConfiguration.registry(registry, kafkaContainer, testContainerProperties);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
