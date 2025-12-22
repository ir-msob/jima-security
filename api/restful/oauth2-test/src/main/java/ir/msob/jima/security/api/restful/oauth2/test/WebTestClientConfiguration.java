package ir.msob.jima.security.api.restful.oauth2.test;

import ir.msob.jima.core.beans.properties.JimaProperties;
import ir.msob.jima.core.commons.condition.ConditionalOnReactiveOrNone;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;


/**
 * This configuration class, WebTestClientConfiguration, is responsible for configuring and customizing the behavior of WebClient instances.
 * It sets up OAuth2 client integration, load balancing, and logging of requests.
 * <p>
 * Author: Yaqub Abdi
 */
@CommonsLog
@Configuration
@RequiredArgsConstructor
@ConditionalOnReactiveOrNone
public class WebTestClientConfiguration {

    private final JimaProperties jimaProperties;

    /**
     * Create an ExchangeFilterFunction for logging HTTP requests.
     *
     * @return The ExchangeFilterFunction that logs requests.
     */
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("=== REQUEST ===");
            log.info("Method: " + clientRequest.method());
            log.info("URL: " + clientRequest.url());
            log.info("Headers: " + clientRequest.headers());
            return Mono.just(clientRequest);
        });
    }

    /**
     * Create a primary WebTestClient instance for making HTTP requests.
     *
     * @param builder The WebTestClient.Builder used to create the WebTestClient instance.
     * @return The primary WebTestClient instance.
     */
    @Bean
    @Primary
    public WebTestClient webTestClient(WebTestClient.Builder builder) {
        return builder.build();
    }

    /**
     * Create a primary WebTestClient.Builder with OAuth2 integration.
     *
     * @param authorizedClientManager The manager for OAuth2 authorized clients.
     * @return The WebTestClient.Builder instance.
     */
    /**
     * Expose the builder. Do NOT create and expose a WebTestClient bean yourself —
     * let Spring Boot create it so it's bound to the random port/test context.
     */
    @Bean
    @Primary
    public WebTestClient.Builder webTestClientBuilder(
            AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager,
            ApplicationContext applicationContext) {

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultOAuth2AuthorizedClient(true);
        oauth.setDefaultClientRegistrationId(jimaProperties.getSecurity().getDefaultClientRegistrationId());

        // bind builder to application context so Spring can produce a WebTestClient bound
        // to the app's random port and use that across tests.
        return WebTestClient
                .bindToApplicationContext(applicationContext)
                .configureClient()
                .filter(oauth)
                .filters(exchangeFilterFunctions -> exchangeFilterFunctions.add(logRequest()));
    }
}
