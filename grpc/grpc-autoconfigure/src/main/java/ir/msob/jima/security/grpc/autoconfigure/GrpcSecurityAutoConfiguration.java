package ir.msob.jima.security.grpc.autoconfigure;

import io.grpc.*;
import ir.msob.jima.platform.api.logger.Logger;
import ir.msob.jima.platform.api.logger.LoggerFactory;
import ir.msob.jima.security.api.util.BearerTokenExtractorUtil;
import ir.msob.jima.security.grpc.api.AuthenticationReader;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@AutoConfiguration
@RequiredArgsConstructor
public class GrpcSecurityAutoConfiguration {

    private static final Logger logger =
            LoggerFactory.getLogger(GrpcSecurityAutoConfiguration.class);
    private static final Metadata.Key<String> AUTHORIZATION_KEY =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);
    private final AuthenticationReader authenticationReader;

    @Bean
    public ServerInterceptor authenticatingServerInterceptor() {

        return new ServerInterceptor() {

            @Override
            public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
                    ServerCall<ReqT, RespT> serverCall,
                    Metadata metadata,
                    ServerCallHandler<ReqT, RespT> serverCallHandler) {

                logger.debug("gRPC authentication interceptor triggered");

                String header = metadata.get(AUTHORIZATION_KEY);

                String token = BearerTokenExtractorUtil.extract(header);

                if (StringUtils.isBlank(token)) {
                    return serverCallHandler.startCall(serverCall, metadata);
                }

                try {
                    Authentication authentication =
                            authenticationReader.authenticate(token);

                    if (authentication == null || !authentication.isAuthenticated()) {
                        throw Status.UNAUTHENTICATED
                                .withDescription("Invalid authentication")
                                .asRuntimeException();
                    }

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                    return serverCallHandler.startCall(serverCall, metadata);

                } catch (Exception e) {
                    SecurityContextHolder.clearContext();

                    logger.error("gRPC authentication failed", e);

                    throw Status.UNAUTHENTICATED
                            .withDescription("Authentication failed")
                            .withCause(e)
                            .asRuntimeException();
                }
            }
        };
    }
}