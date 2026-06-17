package ir.msob.jima.security.autoconfigure.token;

import ir.msob.jima.security.api.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;


@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class TokenReactiveServiceAutoConfiguration {

    @Bean
    TokenReactiveService tokenReactiveService(AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager
            , SecurityProperties securityProperties) {
        return new TokenReactiveService(authorizedClientManager, securityProperties);
    }

}
