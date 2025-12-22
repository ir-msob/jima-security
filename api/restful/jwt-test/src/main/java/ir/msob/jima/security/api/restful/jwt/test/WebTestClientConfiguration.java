package ir.msob.jima.security.api.restful.jwt.test;

import ir.msob.jima.core.commons.condition.ConditionalOnReactiveOrNone;
import ir.msob.jima.core.commons.security.BaseTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

/**
 * Configuration class for setting up a {@link WebTestClient} with JWT authentication support.
 * <p>
 * This configuration automatically adds a Bearer JWT token to every HTTP request made by the WebTestClient.
 * It also provides a logging filter for requests to help with debugging and test validation.
 * <p>
 * The JWT token is obtained from the {@link BaseTokenService}.
 * <p>
 * This configuration is only active in reactive web applications.
 * <p>
 * Author: Yaqub Abdi
 */
@CommonsLog
@Configuration
@RequiredArgsConstructor
@ConditionalOnReactiveOrNone
public class WebTestClientConfiguration {

    private final BaseTokenService tokenService;
    private final ApplicationContext applicationContext;

    /**
     * Creates an {@link ExchangeFilterFunction} that logs HTTP request information.
     * <p>
     * The log includes HTTP method, URL, and headers.
     *
     * @return an ExchangeFilterFunction that logs requests
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
     * Creates an {@link ExchangeFilterFunction} that adds a JWT Bearer token
     * to the Authorization header of each request.
     *
     * @return an ExchangeFilterFunction that adds the Authorization header
     */
    private ExchangeFilterFunction jwtBearerFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            String token = tokenService.getToken();
            var request = org.springframework.web.reactive.function.client.ClientRequest.from(clientRequest)
                    .header("Authorization", "Bearer " + token)
                    .build();
            return Mono.just(request);
        });
    }

    /**
     * Creates a primary {@link WebTestClient.Builder} bound to the application context,
     * configured with JWT Bearer authentication and request logging.
     * <p>
     * Tests can inject this builder and customize it further if needed.
     *
     * @return a configured WebTestClient.Builder
     */
    @Bean
    @Primary
    public WebTestClient.Builder webTestClientBuilder() {
        // obtain the builder from MockServerSpec
        WebTestClient.Builder builder = WebTestClient
                .bindToApplicationContext(applicationContext)
                .configureClient(); // returns WebTestClient.Builder

        // add a single filter
        builder.filter(jwtBearerFilter());

        // or manipulate the whole filters list (ordering/removal etc.)
        builder.filters(filters -> {
            // jwt bearer first
            filters.addFirst(jwtBearerFilter());
            // logging after it
            filters.add(logRequest());
        });

        return builder;
    }


    /**
     * Creates a primary {@link WebTestClient} instance using the configured builder.
     * <p>
     * Optional: tests can inject the builder instead of the client directly.
     *
     * @param builder the configured WebTestClient.Builder
     * @return a WebTestClient instance
     */
    @Bean
    @Primary
    public WebTestClient webTestClient(WebTestClient.Builder builder) {
        return builder.build();
    }
}
