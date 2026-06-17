package ir.msob.jima.security.autoconfigure.token;

import ir.msob.jima.security.api.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;


@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class TokenServiceAutoConfiguration {

    @Bean
    TokenService tokenService(AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager
            , SecurityProperties securityProperties) {
        return new TokenService(authorizedClientManager, securityProperties);
    }

}
