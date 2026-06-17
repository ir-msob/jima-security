package ir.msob.jima.security.autoconfigure.token;

import ir.msob.jima.platform.api.security.BaseTokenService;
import ir.msob.jima.security.api.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;

/**
 * This TokenService class is responsible for retrieving OAuth2 tokens
 * for non-reactive (Servlet-based) applications.
 * <p>
 * Author: Yaqub Abdi
 */
@RequiredArgsConstructor
public class TokenService implements BaseTokenService {

    private final AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager;
    private final SecurityProperties securityProperties;

    /**
     * Retrieves an OAuth2 token for the client.
     *
     * @return The OAuth2 token for the client.
     */
    @SneakyThrows
    @Override
    public String getToken() {
        var authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(
                        securityProperties.getDefaultClientRegistrationId())
                .principal(securityProperties.getDefaultClientRegistrationId())
                .build();

        var authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
            throw new IllegalStateException("Failed to obtain access token for client: "
                    + securityProperties.getDefaultClientRegistrationId());
        }

        return authorizedClient.getAccessToken().getTokenValue();
    }
}
