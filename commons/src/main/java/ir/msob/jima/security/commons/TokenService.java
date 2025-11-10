package ir.msob.jima.security.commons;

import ir.msob.jima.core.beans.properties.JimaProperties;
import ir.msob.jima.core.commons.security.BaseTokenService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.stereotype.Service;

/**
 * This TokenService class is responsible for retrieving OAuth2 tokens
 * for non-reactive (Servlet-based) applications.
 * <p>
 * Author: Yaqub Abdi
 */
@Service
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class TokenService implements BaseTokenService {

    private final AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager;
    private final JimaProperties jimaProperties;

    /**
     * Retrieves an OAuth2 token for the client.
     *
     * @return The OAuth2 token for the client.
     */
    @SneakyThrows
    @Override
    public String getToken() {
        var authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(
                        jimaProperties.getSecurity().getDefaultClientRegistrationId())
                .principal(jimaProperties.getSecurity().getDefaultClientRegistrationId())
                .build();

        var authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
            throw new IllegalStateException("Failed to obtain access token for client: "
                    + jimaProperties.getSecurity().getDefaultClientRegistrationId());
        }

        return authorizedClient.getAccessToken().getTokenValue();
    }
}
