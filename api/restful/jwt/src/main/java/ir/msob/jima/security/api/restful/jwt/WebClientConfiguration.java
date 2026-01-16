package ir.msob.jima.security.api.restful.jwt;

import ir.msob.jima.core.commons.condition.ConditionalOnReactiveOrNone;
import ir.msob.jima.core.commons.logger.Logger;
import ir.msob.jima.core.commons.logger.LoggerFactory;
import ir.msob.jima.core.commons.security.BaseTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * WebClient configuration for JWT-based authentication in reactive Spring applications.
 *
 * <p>This configuration builds a {@link WebClient.Builder} that automatically attaches a freshly
 * generated JWT to every outgoing HTTP request. The JWT is obtained through a provided
 * {@link BaseTokenService}, making the setup suitable for simple non-OAuth2 authentication
 * scenarios where stateless JWT tokens are generated internally by the application.</p>
 *
 * <p>Features:</p>
 * <ul>
 *     <li>Load-balanced WebClient for use in distributed environments</li>
 *     <li>Automatic injection of a newly generated JWT into the Authorization header</li>
 *     <li>Request logging for improved observability</li>
 *     <li>Primary WebClient and WebClient.Builder beans for convenient auto-wiring</li>
 * </ul>
 *
 * <p>The configuration is activated only in reactive web applications.</p>
 *
 * @author Yaqub Abdi
 * @since 1.0.0
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnReactiveOrNone
public class WebClientConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(WebClientConfiguration.class);

    private final BaseTokenService tokenService;

    /**
     * Creates an {@link ExchangeFilterFunction} that logs outgoing HTTP requests.
     *
     * @return an ExchangeFilterFunction that logs the HTTP method and target URL
     */
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            logger.info("Request: " + request.method() + " " + request.url());
            return Mono.just(request);
        });
    }

    /**
     * Creates a {@link WebClient.Builder} bean that:
     * <ul>
     *     <li>is load-balanced for service-to-service communication</li>
     *     <li>automatically adds a newly generated JWT to each outgoing request</li>
     *     <li>logs request details</li>
     * </ul>
     *
     * <p>Before each request is executed, a new JWT is generated using
     * {@link BaseTokenService#getToken()}. The token is then added to the Authorization header
     * as a Bearer token.</p>
     *
     * @return a configured and load-balanced {@link WebClient.Builder}
     */
    @Bean
    @Primary
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter((request, next) -> {

                    // Generate a fresh JWT for each outgoing request
                    String token = tokenService.getToken();

                    // Attach the token to the Authorization header
                    ClientRequest newRequest = ClientRequest.from(request)
                            .header("Authorization", "Bearer " + token)
                            .build();

                    return next.exchange(newRequest);
                })
                .filter(logRequest());
    }

    /**
     * Creates a primary {@link WebClient} instance based on the configured
     * {@link WebClient.Builder}.
     *
     * @param builder the WebClient.Builder configured with JWT filter and logging
     * @return the built {@link WebClient}
     */
    @Bean
    @Primary
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
}
