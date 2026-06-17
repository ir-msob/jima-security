package ir.msob.jima.security.autoconfigure.jwt;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveJwtDecoderAutoConfiguration {

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder(OAuth2ResourceServerProperties properties) {
        return ReactiveJwtDecoders
                .fromIssuerLocation(properties.getJwt().getIssuerUri());
    }
}
