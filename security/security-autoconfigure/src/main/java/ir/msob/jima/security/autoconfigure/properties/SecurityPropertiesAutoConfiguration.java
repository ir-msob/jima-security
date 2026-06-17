package ir.msob.jima.security.autoconfigure.properties;

import ir.msob.jima.security.api.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class SecurityPropertiesAutoConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "jima.security")
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }
}
