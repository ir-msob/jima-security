package ir.msob.jima.security.api.restful.oauth2;

import ir.msob.jima.core.beans.properties.JimaProperties;
import ir.msob.jima.core.commons.condition.ConditionalOnReactiveOrNone;
import ir.msob.jima.core.commons.logger.Logger;
import ir.msob.jima.core.commons.logger.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


/**
 * This configuration class, WebClientConfiguration, is responsible for configuring and customizing the behavior of WebClient instances.
 * It sets up OAuth2 client integration, load balancing, and logging of requests.
 * <p>
 * Author: Yaqub Abdi
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnReactiveOrNone
public class WebClientConfiguration {

    private final JimaProperties jimaProperties;
    private static final Logger logger = LoggerFactory.getLogger(WebClientConfiguration.class);

    /**
     * Create an ExchangeFilterFunction for logging HTTP requests.
     *
     * @return The ExchangeFilterFunction that logs requests.
     */
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("Request: " + clientRequest.method() + " " + clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    /**
     * Create a primary WebClient instance for making HTTP requests.
     *
     * @param builder The WebClient.Builder used to create the WebClient instance.
     * @return The primary WebClient instance.
     */
    @Bean
    @Primary
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    /**
     * Create a primary load-balanced WebClient.Builder with OAuth2 integration.
     *
     * @param authorizedClientManager The manager for OAuth2 authorized clients.
     * @return The load-balanced WebClient.Builder instance.
     */
    @Bean
    @Primary
    @LoadBalanced
    public WebClient.Builder webClientBuilder(AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        // Create a ServerOAuth2AuthorizedClientExchangeFilterFunction to handle OAuth2 integration.
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultOAuth2AuthorizedClient(true);
        oauth.setDefaultClientRegistrationId(jimaProperties.getSecurity().getDefaultClientRegistrationId());

        return WebClient.builder()
                .filter(oauth)  // Apply OAuth2 client integration.
                .filters(exchangeFilterFunctions -> exchangeFilterFunctions.add(logRequest()));  // Log HTTP requests.
    }
}
