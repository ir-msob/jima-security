package ir.msob.jima.security.rsocket.autoconfigure;

import io.rsocket.metadata.WellKnownMimeType;
import ir.msob.jima.platform.api.security.BaseTokenService;
import ir.msob.jima.platform.rsocket.api.BaseRSocketRequesterMetadata;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.rsocket.metadata.BearerTokenMetadata;
import org.springframework.util.MimeTypeUtils;

@AutoConfiguration
public class RSocketRequesterMetadataAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(BaseRSocketRequesterMetadata.class)
    public BaseRSocketRequesterMetadata rSocketRequesterMetadata(BaseTokenService tokenService) {
        return metadataSpec -> {
            BearerTokenMetadata token =
                    new BearerTokenMetadata(tokenService.getToken());

            metadataSpec.metadata(
                    token,
                    MimeTypeUtils.parseMimeType(
                            WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString()
                    )
            );
        };
    }

}