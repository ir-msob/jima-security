package ir.msob.jima.security.api.rsocket.oauth2;

import io.rsocket.SocketAcceptor;
import ir.msob.jima.core.api.rsocket.commons.BaseRSocketRequesterBuilder;
import ir.msob.jima.core.beans.properties.JimaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.rsocket.metadata.BearerTokenAuthenticationEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class is responsible for building RSocket requesters with OAuth2 authentication.
 */
@Component
@RequiredArgsConstructor
public class RSocketOauth2RequesterBuilder implements BaseRSocketRequesterBuilder {
    private final RSocketRequester.Builder rsocketRequesterBuilder;
    private final RSocketStrategies rsocketStrategies;
    private final JimaProperties jimaProperties;

    /**
     * Returns a builder for RSocketRequester with OAuth2 authentication.
     *
     * @param candidateHandlers Handlers to be used for handling RSocket messages.
     * @return RSocketRequester.Builder instance with OAuth2 authentication.
     */
    @Override
    public RSocketRequester.Builder getBuilder(Object... candidateHandlers) {
        return rsocketRequesterBuilder
                .rsocketStrategies(rsocketStrategies.mutate().encoder(new BearerTokenAuthenticationEncoder()).build())
                .rsocketConnector(connector -> connector.acceptor(getSocketAcceptor(List.of(candidateHandlers))).reconnect(jimaProperties.getClient().getRetryConnection().createRetryBackoffSpec()));
    }

    /**
     * Returns a SocketAcceptor with the provided candidate handlers.
     *
     * @param candidateHandlers Handlers to be used for handling RSocket messages.
     * @return SocketAcceptor instance with the provided candidate handlers.
     */
    private SocketAcceptor getSocketAcceptor(List<Object> candidateHandlers) {
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.setHandlers(candidateHandlers);
        handler.setRSocketStrategies(this.rsocketStrategies);
        handler.afterPropertiesSet();
        return handler.responder();
    }
}