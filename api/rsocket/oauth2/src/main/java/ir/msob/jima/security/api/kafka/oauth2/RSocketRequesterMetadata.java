package ir.msob.jima.security.api.kafka.oauth2;

import io.rsocket.metadata.WellKnownMimeType;
import ir.msob.jima.core.api.rsocket.commons.BaseRSocketRequesterMetadata;
import ir.msob.jima.core.commons.security.BaseTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.rsocket.RSocketRequester;
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
    private final BaseTokenService tokenService;

    /**
     * Adds metadata to the provided RSocketRequester.MetadataSpec instance.
     *
     * @param metadataSpec The RSocketRequester.MetadataSpec instance to which the metadata will be added.
     */
    @Override
    public void metadata(RSocketRequester.MetadataSpec<?> metadataSpec) {
        BearerTokenMetadata token = new BearerTokenMetadata(tokenService.getToken());
        MimeType authenticationMimeType =
                MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());
        metadataSpec.metadata(token, authenticationMimeType);
    }
}