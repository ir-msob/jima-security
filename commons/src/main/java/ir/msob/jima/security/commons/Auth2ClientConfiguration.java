package ir.msob.jima.security.commons;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

/**
 * This configuration class, Auth2ClientServletConfiguration, is responsible for configuring
 * OAuth2 client support for authenticated REST (Servlet-based) requests.
 * <p>
 * Author: Yaqub Abdi
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class Auth2ClientConfiguration {

    /**
     * Create an AuthorizedClientServiceOAuth2AuthorizedClientManager to manage OAuth2 authorized clients.
     *
     * @param clientRegistrationRepository  The repository containing client registrations.
     * @param oAuth2AuthorizedClientService The service for managing OAuth2 authorized clients.
     * @return An instance of AuthorizedClientServiceOAuth2AuthorizedClientManager.
     */
    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {

        // Define an OAuth2 authorized client provider for client credentials grant.
        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        // Create the AuthorizedClientServiceOAuth2AuthorizedClientManager.
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, oAuth2AuthorizedClientService);

        // Set the authorized client provider for the manager.
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}
