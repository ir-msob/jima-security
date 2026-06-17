package ir.msob.jima.security.rsocket.autoconfigure;

import ir.msob.jima.platform.api.properties.ClientProperties;
import ir.msob.jima.platform.rsocket.api.BaseRSocketRequesterBuilder;
import ir.msob.jima.security.rsocket.reactive.RSocketOauth2RequesterBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;

@AutoConfiguration
@ConditionalOnClass(RSocketRequester.class)
public class RSocketRequesterBuilderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(BaseRSocketRequesterBuilder.class)
    public BaseRSocketRequesterBuilder rSocketOauth2RequesterBuilder(
            RSocketRequester.Builder requesterBuilder,
            RSocketStrategies rsocketStrategies,
            ClientProperties clientProperties
    ) {
        return new RSocketOauth2RequesterBuilder(
                requesterBuilder,
                rsocketStrategies,
                clientProperties
        );
    }

}