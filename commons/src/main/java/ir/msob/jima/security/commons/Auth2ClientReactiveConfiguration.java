package ir.msob.jima.security.commons;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

/**
 * This configuration class, Auth2ClientConfiguration, is responsible for configuring OAuth2 client support for making authenticated requests in a web client.
 * <p>
 * Author: Yaqub Abdi
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class Auth2ClientReactiveConfiguration {

    /**
     * Create an AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager to manage OAuth2 authorized clients.
     *
     * @param reactiveClientRegistrationRepository  The repository containing client registrations.
     * @param reactiveOAuth2AuthorizedClientService The service for managing OAuth2 authorized clients.
     * @return An instance of AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager.
     */
    @Bean
    public AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository reactiveClientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService) {

        // Define an OAuth2 authorized client provider for client credentials grant.
        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        // Create the AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager.
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                reactiveClientRegistrationRepository, reactiveOAuth2AuthorizedClientService);

        // Set the authorized client provider for the manager.
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}
