package ir.msob.jima.security.autoconfigure.token;

import ir.msob.jima.platform.api.security.BaseTokenService;
import ir.msob.jima.security.api.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;

@RequiredArgsConstructor
public class TokenReactiveService implements BaseTokenService {
    private final AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    private final SecurityProperties securityProperties;

    /**
     * Retrieves an OAuth2 token for the client.
     *
     * @return The OAuth2 token for the client.
     */
    @SneakyThrows
    @Override
    public String getToken() {
        return authorizedClientManager.authorize(
                        OAuth2AuthorizeRequest.withClientRegistrationId(securityProperties.getDefaultClientRegistrationId())
                                .principal(securityProperties.getDefaultClientRegistrationId())
                                .build())
                .map(authorizedClient ->
                        authorizedClient.getAccessToken().getTokenValue()
                ).toFuture().get();
    }
}
