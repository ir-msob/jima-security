package ir.msob.jima.security.commons;

import ir.msob.jima.core.beans.properties.JimaProperties;
import ir.msob.jima.core.commons.security.BaseTokenService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService implements BaseTokenService {
    private final AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    private final JimaProperties jimaProperties;

    /**
     * Retrieves an OAuth2 token for the client.
     *
     * @return The OAuth2 token for the client.
     */
    @SneakyThrows
    @Override
    public String getToken() {
        return authorizedClientManager.authorize(
                        OAuth2AuthorizeRequest.withClientRegistrationId(jimaProperties.getSecurity().getDefaultClientRegistrationId())
                                .principal(jimaProperties.getSecurity().getDefaultClientRegistrationId())
                                .build())
                .map(authorizedClient ->
                        authorizedClient.getAccessToken().getTokenValue()
                ).toFuture().get();
    }
}
