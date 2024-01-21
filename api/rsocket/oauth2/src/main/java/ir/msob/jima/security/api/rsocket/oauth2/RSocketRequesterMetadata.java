package ir.msob.jima.security.api.rsocket.oauth2;

import io.rsocket.metadata.WellKnownMimeType;
import ir.msob.jima.core.api.rsocket.commons.BaseRSocketRequesterMetadata;
import ir.msob.jima.core.beans.configuration.JimaConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.rsocket.metadata.BearerTokenMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

/**
 * This class is responsible for managing RSocket requester metadata with OAuth2 authentication.
 */
@Component
@RequiredArgsConstructor
public class RSocketRequesterMetadata implements BaseRSocketRequesterMetadata {
    private final AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    private final JimaConfigProperties jimaConfigProperties;

    /**
     * Adds metadata to the provided RSocketRequester.MetadataSpec instance.
     *
     * @param metadataSpec The RSocketRequester.MetadataSpec instance to which the metadata will be added.
     */
    @Override
    public void metadata(RSocketRequester.MetadataSpec<?> metadataSpec) {
        BearerTokenMetadata token = new BearerTokenMetadata(getTokenForClient());
        MimeType authenticationMimeType =
                MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());
        metadataSpec.metadata(token, authenticationMimeType);
    }

    /**
     * Retrieves an OAuth2 token for the client.
     *
     * @return The OAuth2 token for the client.
     */
    @SneakyThrows
    public String getTokenForClient() {
        return authorizedClientManager.authorize(
                        OAuth2AuthorizeRequest.withClientRegistrationId(jimaConfigProperties.getSecurity().getDefaultClientRegistrationId())
                                .principal(jimaConfigProperties.getSecurity().getDefaultClientRegistrationId())
                                .build())
                .map(authorizedClient ->
                        authorizedClient.getAccessToken().getTokenValue()
                ).toFuture().get();
    }
}